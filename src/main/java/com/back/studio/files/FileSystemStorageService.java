package com.back.studio.files;

import com.luciad.imageio.webp.WebPReadParam;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {

        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(MultipartFile file) {
        try {
            String fileExtension = getNewName(file);

            String newFilename = UUID.randomUUID() + fileExtension;

            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(newFilename))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return newFilename;
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    private static String getNewName(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        // Generate a random UUID for the file name
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";

        // Extract file extension
        if (originalFilename != null && originalFilename.contains(".")) {
            int dotIndex = originalFilename.lastIndexOf('.');
            fileExtension = originalFilename.substring(dotIndex);
        }
        return fileExtension;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    private String extractFilenameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    @Override
    public Resource findOrCreateImage(String src, String width) {
        try {
            String filename = extractFilenameFromUrl(src);
            Path resizedFilePath = null;
            Path originalFilePath = load(filename);

            // Проверка, нужно ли изменять размер изображения
            if (width != null && !width.equals("undefined")) {
                String resizedFilename = filename.replace(".", "_w" + width + ".");
                resizedFilePath = load(resizedFilename);

                // Проверка, существует ли файл с нужным разрешением
                if (Files.exists(resizedFilePath)) {
                    return new UrlResource(resizedFilePath.toUri());
                }
            }

            // Если оригинальный файл не существует, но src - это URL
            if (!Files.exists(originalFilePath) && src.startsWith("http")) {
                // Скачивание оригинального файла
                downloadFile(src, originalFilePath);
            }

            // Если файл все еще не существует после попытки загрузки
            if (!Files.exists(originalFilePath)) {
                throw new StorageFileNotFoundException("File not found: " + filename);
            }

            // Проверка типа файла
            String fileExtension = getFileExtension(filename);
            if (fileExtension != null && fileExtension.equalsIgnoreCase("webp")) {
                // Конвертация изображения из WebP в JPEG
                convertWebpToJpeg(originalFilePath, originalFilePath.resolveSibling(filename.replace(".webp", ".jpg")));
                originalFilePath = originalFilePath.resolveSibling(filename.replace(".webp", ".jpg"));
            }

            // Если нужно изменять размер изображения и файл не был найден
            if (resizedFilePath == null) {
                return new UrlResource(originalFilePath.toUri());
            }

            // Изменение размера изображения
            resizeImage(originalFilePath, resizedFilePath, Integer.parseInt(width));

            // Возвращение ресурса
            return new UrlResource(resizedFilePath.toUri());
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + src, e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing file: " + src, e);
        }
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0) {
            return filename.substring(dotIndex + 1);
        }
        return null;
    }

    private void convertWebpToJpeg(Path sourcePath, Path destinationPath) throws IOException {
        ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
        WebPReadParam readParam = new WebPReadParam();
        readParam.setBypassFiltering(true);
        reader.setInput(new FileImageInputStream(sourcePath.toFile()));
        BufferedImage image = reader.read(0, readParam);
        ImageIO.write(image, "jpg", destinationPath.toFile());
    }

    private void downloadFile(String urlStr, Path destination) throws IOException {
        try (InputStream in = new URL(urlStr).openStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void resizeImage(Path sourcePath, Path destinationPath, int width) throws IOException {
        Thumbnails.of(sourcePath.toFile())
                .width(width)
                    .toFile(destinationPath.toFile());
    }
    /*private void resizeImage(Path sourcePath, Path destinationPath, int width) throws IOException {
        try (InputStream inputStream = Files.newInputStream(sourcePath);
             OutputStream outputStream = Files.newOutputStream(destinationPath)) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage != null) {
                int height = (int) ((double) bufferedImage.getHeight() * width / bufferedImage.getWidth());
                BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = resizedImage.createGraphics();
                graphics.drawImage(bufferedImage, 0, 0, width, height, null);
                graphics.dispose();
                ImageIO.write(resizedImage, "JPEG", outputStream);
            } else {
                throw new IOException("Failed to read image from source: " + sourcePath);
            }
        }
    }*/
}
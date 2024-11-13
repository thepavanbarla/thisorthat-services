package com.tot.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.amazonaws.services.s3.model.S3Object;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tot.pojo.RestResponse;
import com.tot.utils.UserIdentityUtils;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j public class MediaManagerService {

    @Value("${media.s3.bucket.name}") private String bucketName;

    @Value("${s3.users.image.path}") private String s3UsersImagePath;

    @Value("${s3.posts.image.path}") private String s3PostsImagePath;

    @Value("${s3.tiny.image.width}") private int s3TinyImageWidth;

    @Value("${s3.post.small.image.width}") private int s3PostSmallImageWidth;

    @Value("${s3.post.medium.image.width}") private int s3PostMediumImageWidth;

    @Value("${s3.post.full.image.width}") private int s3PostFullImageWidth;

    @Value("${s3.user.small.image.width}") private int s3UserSmallImageWidth;

    @Value("${s3.user.full.image.width}") private int s3UserFullImageWidth;

    @Autowired private AmazonS3 amazonS3;

    public RestResponse uploadUserPicture(MultipartFile multipartFile) {
        String requestUserId = UserIdentityUtils.getUserIdFromSecurityContext();
        String fileName = null;
        File file = null;
        try {
            file = convertMultiPartFileToFile(multipartFile);
            fileName =
                uploadFileToS3Bucket(bucketName, file, requestUserId, s3UsersImagePath, false);

        } catch (AmazonServiceException ex) {
            log.error("Error uploading file to S3", ex);
        }
        return RestResponse.getSuccessResponse(fileName);
    }

    public RestResponse uploadPostPicture(MultipartFile multipartFile) {
        String requestUserId = UserIdentityUtils.getUserIdFromSecurityContext();
        String fileName = null;
        File file = null;
        try {
            file = convertMultiPartFileToFile(multipartFile);
            fileName =
                uploadFileToS3Bucket(bucketName, file, requestUserId, s3PostsImagePath, true);
        } catch (AmazonServiceException ex) {
            log.error("Error uploading file to S3", ex);
        }

        return RestResponse.getSuccessResponse(fileName);
    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException ex) {

        }
        return file;
    }

    private String uploadFileToS3Bucket(final String bucketName, final File file, String userId,
        String s3Path, boolean isPostImage) {
        String fileNamePrefix =
            new StringBuilder(UUID.randomUUID().toString()).append("_").append(LocalDateTime.now())
                .toString();
        String fileExtn = getFileExtension(file.getName());
        String origfilePath =
            new StringBuilder(s3Path).append(userId).append("/").append(fileNamePrefix).append(".")
                .append(fileExtn).toString();
        //        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, origfilePath, file);
        //        amazonS3.putObject(putObjectRequest);

        log.info("Before upload");
        generateAndUploadAlternateImage(bucketName, file, s3Path, fileNamePrefix, fileExtn,
            origfilePath, userId, s3TinyImageWidth, "tiny");
        log.info("Uploaded tiny image");

        //        CompletableFuture.runAsync(() -> {
        try {
            if (isPostImage) {
                generateAndUploadAlternateImage(bucketName, file, s3Path, fileNamePrefix, fileExtn,
                    origfilePath, userId, s3PostMediumImageWidth, "medium");
                log.info("Uploaded medium post image");
                generateAndUploadAlternateImage(bucketName, file, s3Path, fileNamePrefix, fileExtn,
                    origfilePath, userId, s3PostSmallImageWidth, "small");
                log.info("Uploaded small post image");
                generateAndUploadAlternateImage(bucketName, file, s3Path, fileNamePrefix, fileExtn,
                    origfilePath, userId, s3PostFullImageWidth, null);
                log.info("Uploaded full post image");
            } else {
                generateAndUploadAlternateImage(bucketName, file, s3Path, fileNamePrefix, fileExtn,
                    origfilePath, userId, s3UserSmallImageWidth, "small");
                log.info("Uploaded small user image");
                generateAndUploadAlternateImage(bucketName, file, s3Path, fileNamePrefix, fileExtn,
                    origfilePath, userId, s3UserFullImageWidth, null);
                log.info("Uploaded full user image");
            }
            file.delete();
        } catch (Exception ex) {

        } finally {
            if (file != null) {
                try {
                    file.delete();
                } catch (Exception e) {
                }
            }
        }
        //        });
        return origfilePath;
    }

    private void generateAndUploadAlternateImage(String bucketName, File file, String s3Path,
        String fileNamePrefix, String fileExtn, String origfilePath, String userId,
        int alternateWidth, String alternateSizeName) {
        BufferedImage orig = null;
        try {
            orig = ImageIO.read(file);
        } catch (IOException e) {
            log.error("Error while reading original image: " + origfilePath);
        }
        BufferedImage alternateImage = resizeImage(orig, alternateWidth);
        String alternateFileName =
            new StringBuilder(fileNamePrefix).append(".").append(fileExtn).toString();
        String alternateFilePath =
            new StringBuilder(s3Path).append(alternateSizeName != null ? alternateSizeName : "")
                .append(alternateSizeName != null ? "/" : "").append(userId).append("/")
                .append(alternateFileName).toString();
        File alternateFile = null;
        try {
            alternateFile = new File(alternateFileName);
            ImageIO.write(alternateImage, fileExtn, alternateFile);
            PutObjectRequest putAlternateObjectRequest =
                new PutObjectRequest(bucketName, alternateFilePath, alternateFile);
            amazonS3.putObject(putAlternateObjectRequest);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error while creating alternate image for: " + origfilePath);
        } finally {
            if (alternateFile != null) {
                try {
                    alternateFile.delete();
                } catch (Exception e) {
                }
            }
        }
    }

    private String getFileExtension(String fileName) {
        return Optional.ofNullable(fileName).filter(f -> f.contains("."))
            .map(f -> f.substring(fileName.lastIndexOf(".") + 1)).get();
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth) {
        return Scalr.resize(originalImage, targetWidth);
    }

    //returns path of merged file
    public RestResponse mergePostImages(String s3Path1, String s3Path2, String postId, String userId, boolean isSmall)
        throws IOException {
        String mergedPath = "post-pictures/merged/" + userId + "/" + (isSmall ? "small/" : "");
        String mergedName = postId + ".jpg";
        if (amazonS3.doesObjectExist(bucketName, mergedPath + mergedName)) {
            log.info("Merged image exists, returning the path: " + mergedPath + mergedName);
            return RestResponse.getSuccessResponse(mergedPath + mergedName);
        }
        S3Object obj1 = amazonS3.getObject(bucketName, s3Path1);
        S3Object obj2 = amazonS3.getObject(bucketName, s3Path2);
        BufferedImage img1 = ImageIO.read(obj1.getObjectContent());
        BufferedImage img2 = ImageIO.read(obj2.getObjectContent());

        BufferedImage combined = stitchImages(img1, img2);
        File combinedFile = null;
        try {
            combinedFile = new File(mergedName);
            ImageIO.write(combined, "png", combinedFile);
            PutObjectRequest putCombinedObject =
                new PutObjectRequest(bucketName, mergedPath + mergedName, combinedFile);
            amazonS3.putObject(putCombinedObject);
            log.info("Successfully created merged image for post: " + postId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error while creating combined image for: " + mergedPath + mergedName);
        } finally {
            if (combinedFile != null) {
                try {
                    combinedFile.delete();
                } catch (Exception e) {
                }
            }
        }
        return RestResponse.getSuccessResponse(mergedPath + mergedName);
    }

    private BufferedImage stitchImages(BufferedImage image1, BufferedImage image2) {
        // Create a new image that is the width of the two input images combined
        int combinedWidth = image1.getWidth() + image2.getWidth();
        int maxHeight = Math.max(image1.getHeight(), image2.getHeight());
        BufferedImage combinedImage =
            new BufferedImage(combinedWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);

        // Draw the first image onto the combined image
        Graphics2D g2d = combinedImage.createGraphics();
        g2d.drawImage(image1, 0, 0, null);

        // Draw the second image next to the first image
        g2d.drawImage(image2, image1.getWidth(), 0, null);
        g2d.dispose();
        return combinedImage;
    }

}

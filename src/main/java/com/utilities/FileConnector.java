package com.utilities;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.configuration.BaseTest.printMsgOnConsole;

public class FileConnector {

	/**
	 * Create directory at specific path
	 */
	public static void createDir(String directoryPath) {
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			printMsgOnConsole("Creating directory at " + directoryPath);
			directory.mkdirs();
		}
	}

	/**
	 * Delete directory from specific path
	 */
	public static void deleteDir(String path) {
		Path directory = Paths.get(path);
		try {
			Files.walkFileTree(directory, new SimpleFileVisitor<>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
					Files.delete(file); // this will work because it's always a File
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir); //this will work because Files in the directory are already deleted
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			printMsgOnConsole("-");
		}
	}

	/**
	 * Get current Date
	 */
	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Get String from Array
	 */
	public static String getStringFromArray(String[] arr) {
		StringBuilder str = new StringBuilder();
		for (String s : arr) {
			str.append(s).append(",");
		}
		if (str.toString().endsWith(",")) {
			str = new StringBuilder(str.substring(0, str.length() - 1));
		}
		return str.toString();
	}

	public static void zipDirectory(Path sourceDirPath, Path zipFilePath) throws IOException {
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()))) {
			Files.walk(sourceDirPath)
					.filter(path -> !Files.isDirectory(path))
					.forEach(path -> {
						ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
						try (FileInputStream fileInputStream = new FileInputStream(path.toFile())) {
							zipOutputStream.putNextEntry(zipEntry);
							byte[] buffer = new byte[1024];
							int length;
							while ((length = fileInputStream.read(buffer)) >= 0) {
								zipOutputStream.write(buffer, 0, length);
							}
							zipOutputStream.closeEntry();
						} catch (IOException e) {
							System.err.println("Error processing file: " + path);
							e.printStackTrace();
						}
					});
		}
	}

	public static void copyDirectory(Path sourceDir, Path targetDir) throws IOException {
		Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				Path targetPath = targetDir.resolve(sourceDir.relativize(dir));
				if (!Files.exists(targetPath)) {
					Files.createDirectory(targetPath);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.copy(file, targetDir.resolve(sourceDir.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	public static int getRandomInt(int min, int max) {

		// Usually this should be a field rather than a method variable so
		// that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive

        return rand.nextInt((max - min) + 1) + min;
	}
}

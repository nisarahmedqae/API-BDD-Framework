package com.nahmed.constants;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FrameworkConstants {

    private FrameworkConstants() {
    }

    private static final String RESOURCES_FOLDER_PATH = Path.of(System.getProperty("user.dir"), "src", "test", "resources").toString();
    private static final String CONFIG_FILE_PATH = Path.of(RESOURCES_FOLDER_PATH, "config.properties").toString();
    private static final String EXTENT_REPORT_FOLDER_PATH = Path.of(System.getProperty("user.dir"), "reports", "extent").toString();
    private static final String LOGS_FOLDER_PATH = Path.of(System.getProperty("user.dir"), "reports", "logs").toString();
    private static final String SCHEMA_FOLDER_PATH = System.getProperty("user.dir") + "\\src\\main\\java\\com\\nahmed\\models\\schema\\";
    private static final String DATA_STORE_FILE_PATH = RESOURCES_FOLDER_PATH + "\\data_store.properties";
    private static final DateTimeFormatter REPORT_FOLDER_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final DateTimeFormatter REPORT_DATE_FOLDER_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final int REPORT_RETENTION_MONTHS = 3;

    private static volatile String extentReportFilePath = "";

    public static synchronized String getExtentReportFilePath() {
        if (extentReportFilePath.isEmpty()) {
            purgeOldExtentReports();
            purgeOldLogs();
            extentReportFilePath = createReportPath();
            createParentDirectoryIfMissing(extentReportFilePath);
        }
        return extentReportFilePath;
    }

    private static String createReportPath() {
        String dateFolder = LocalDate.now().format(REPORT_DATE_FOLDER_FORMAT);
        String timestamp = LocalDateTime.now().format(REPORT_FOLDER_TIME_FORMAT);
        return Path.of(EXTENT_REPORT_FOLDER_PATH, dateFolder, timestamp + ".html").toString();
    }

    /**
     * Deletes Extent HTML reports older than {@value REPORT_RETENTION_MONTHS} months.
     */
    private static void purgeOldExtentReports() { purgeOldFiles(new File(EXTENT_REPORT_FOLDER_PATH), ".html"); }

    /**
     * Deletes log files older than {@value REPORT_RETENTION_MONTHS} months.
     */
    private static void purgeOldLogs() { purgeOldFiles(new File(LOGS_FOLDER_PATH), ".log"); }

    /**
     * Deletes files with the given extension whose names parse as timestamps older than
     * {@value REPORT_RETENTION_MONTHS} months.
     */
    private static void purgeOldFiles(File folder, String extension) {
        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }

        LocalDateTime cutoff = LocalDateTime.now().minusMonths(REPORT_RETENTION_MONTHS);

        File[] entries = folder.listFiles();
        if (entries == null) {
            return;
        }

        for (File entry : entries) {
            if (entry.isDirectory()) {
                purgeOldFiles(entry, extension);

                File[] remaining = entry.listFiles();
                if (remaining != null && remaining.length == 0 && !entry.delete()) {
                    System.err.println("[FrameworkConstants] Failed to delete empty folder: " + entry.getAbsolutePath());
                }
                continue;
            }

            if (!entry.getName().endsWith(extension)) {
                continue;
            }

            String name = entry.getName().replace(extension, "");
            try {
                LocalDateTime fileDate = LocalDateTime.parse(name, REPORT_FOLDER_TIME_FORMAT);
                if (fileDate.isBefore(cutoff) && !entry.delete()) {
                    System.err.println("[FrameworkConstants] Failed to delete old file: " + entry.getAbsolutePath());
                }
            } catch (Exception ignored) {
                // skip files that don't match the timestamp naming pattern
            }
        }
    }

    private static void createParentDirectoryIfMissing(String reportFilePath) {
        File parentFolder = new File(reportFilePath).getParentFile();
        if (parentFolder != null && !parentFolder.exists() && !parentFolder.mkdirs()) {
            throw new IllegalStateException("Unable to create report directory: " + parentFolder.getAbsolutePath());
        }
    }

    public static String getConfigFilePath() {
        return CONFIG_FILE_PATH;
    }

    public static String getSchemaFolderPath() {
        return SCHEMA_FOLDER_PATH;
    }

    public static String getDataStoreFilePath() {
        return DATA_STORE_FILE_PATH;
    }

}
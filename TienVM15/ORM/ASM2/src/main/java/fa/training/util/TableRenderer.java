package fa.training.util;

import java.util.List;

/**
 * Utility to render data as beautiful ASCII/Unicode tables in the console.
 */
public class TableRenderer {

    /**
     * Prints a beautiful Unicode table.
     *
     * @param title   Optional title printed above the table
     * @param headers List of column headers
     * @param rows    List of rows, each containing cells corresponding to the headers
     */
    public static void printTable(String title, List<String> headers, List<List<String>> rows) {
        if (headers == null || headers.isEmpty()) {
            return;
        }

        // Calculate column widths
        int numCols = headers.size();
        int[] colWidths = new int[numCols];

        for (int i = 0; i < numCols; i++) {
            colWidths[i] = headers.get(i).length();
        }

        for (List<String> row : rows) {
            for (int i = 0; i < numCols; i++) {
                if (row != null && i < row.size()) {
                    String cell = row.get(i);
                    if (cell != null && cell.length() > colWidths[i]) {
                        colWidths[i] = cell.length();
                    }
                }
            }
        }

        // Generate lines
        StringBuilder topBorder = new StringBuilder("┌");
        StringBuilder separator = new StringBuilder("├");
        StringBuilder bottomBorder = new StringBuilder("└");

        for (int i = 0; i < numCols; i++) {
            topBorder.append("─".repeat(colWidths[i] + 2));
            separator.append("─".repeat(colWidths[i] + 2));
            bottomBorder.append("─".repeat(colWidths[i] + 2));

            if (i < numCols - 1) {
                topBorder.append("┬");
                separator.append("┼");
                bottomBorder.append("┴");
            } else {
                topBorder.append("┐");
                separator.append("┤");
                bottomBorder.append("┘");
            }
        }

        // Print Title
        if (title != null && !title.trim().isEmpty()) {
            System.out.println("\n✨ " + title.toUpperCase() + " ✨");
        }

        // Print Top Border
        System.out.println(topBorder);

        // Print Header Row
        StringBuilder headerRow = new StringBuilder("│");
        for (int i = 0; i < numCols; i++) {
            headerRow.append(String.format(" %-" + colWidths[i] + "s │", headers.get(i)));
        }
        System.out.println(headerRow);

        // Print Header Separator
        System.out.println(separator);

        // Print Data Rows
        if (rows.isEmpty()) {
            StringBuilder emptyRow = new StringBuilder("│");
            int totalWidth = topBorder.length() - 2;
            String emptyMsg = "No data available";
            if (emptyMsg.length() > totalWidth - 2) {
                emptyMsg = emptyMsg.substring(0, Math.max(0, totalWidth - 5)) + "...";
            }
            int paddingRight = totalWidth - 2 - emptyMsg.length();
            emptyRow.append(" ").append(emptyMsg).append(" ".repeat(Math.max(0, paddingRight))).append(" │");
            System.out.println(emptyRow);
        } else {
            for (List<String> row : rows) {
                StringBuilder dataRow = new StringBuilder("│");
                for (int i = 0; i < numCols; i++) {
                    String cell = (row != null && i < row.size() && row.get(i) != null) ? row.get(i) : "";
                    dataRow.append(String.format(" %-" + colWidths[i] + "s │", cell));
                }
                System.out.println(dataRow);
            }
        }

        // Print Bottom Border
        System.out.println(bottomBorder);
    }
}

package fa.training.util;

import java.util.List;

public class TableRenderer {

    public static void printTable(String title, List<String> headers, List<List<String>> rows) {
        if (headers == null || headers.isEmpty()) {
            return;
        }

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

        if (title != null && !title.trim().isEmpty()) {
            System.out.println("\n✨ " + title.toUpperCase() + " ✨");
        }

        System.out.println(topBorder);

        StringBuilder headerRow = new StringBuilder("│");
        for (int i = 0; i < numCols; i++) {
            headerRow.append(String.format(" %-" + colWidths[i] + "s │", headers.get(i)));
        }
        System.out.println(headerRow);

        System.out.println(separator);

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

        System.out.println(bottomBorder);
    }
}

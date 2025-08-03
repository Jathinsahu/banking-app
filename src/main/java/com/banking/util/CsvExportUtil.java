package com.banking.util;

import com.banking.dto.TransactionResponse;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for exporting data to CSV format
 */
@Component
public class CsvExportUtil {
    
    private static final String CSV_SEPARATOR = ",";
    private static final String CSV_HEADER = "Transaction ID,Sender,Receiver,Amount,Date,Type,Description\n";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Export transaction history to CSV format
     * @param transactions List of transactions to export
     * @return CSV content as string
     */
    public String exportTransactionsToCsv(List<TransactionResponse> transactions) {
        StringWriter csvWriter = new StringWriter();
        
        // Write CSV header
        csvWriter.append(CSV_HEADER);
        
        // Write transaction data
        for (TransactionResponse transaction : transactions) {
            csvWriter.append(String.valueOf(transaction.getId()));
            csvWriter.append(CSV_SEPARATOR);
            
            csvWriter.append(escapeSpecialCharacters(transaction.getSenderUsername() != null ? 
                    transaction.getSenderUsername() : "N/A"));
            csvWriter.append(CSV_SEPARATOR);
            
            csvWriter.append(escapeSpecialCharacters(transaction.getReceiverUsername() != null ? 
                    transaction.getReceiverUsername() : "N/A"));
            csvWriter.append(CSV_SEPARATOR);
            
            csvWriter.append(String.valueOf(transaction.getAmount()));
            csvWriter.append(CSV_SEPARATOR);
            
            csvWriter.append(transaction.getTimestamp().format(DATE_FORMATTER));
            csvWriter.append(CSV_SEPARATOR);
            
            csvWriter.append(transaction.getType().toString());
            csvWriter.append(CSV_SEPARATOR);
            
            csvWriter.append(escapeSpecialCharacters(transaction.getDescription() != null ? 
                    transaction.getDescription() : ""));
            csvWriter.append("\n");
        }
        
        return csvWriter.toString();
    }
    
    /**
     * Escape special characters in CSV fields
     * @param data The data to escape
     * @return Escaped data
     */
    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            return "";
        }
        
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
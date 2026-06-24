package fa.training.TrainingMaterialManagementSystem.utils.validator;

import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import fa.training.TrainingMaterialManagementSystem.exception.FileTooLargeException;
import fa.training.TrainingMaterialManagementSystem.exception.InvalidFileTypeException;

@Component
public class FileValidator {

    private static final long MAX_SIZE = 10 * 1024 * 1024;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "pdf",
            "docx",
            "zip",
            "pptx",
            "ppt",
            "xls",
            "xlsx",
            "txt");

    public void validate(MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("File required");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new FileTooLargeException(
                    "Maximum file size is 10MB");
        }

        String ext = FilenameUtils.getExtension(
                file.getOriginalFilename());

        if (!ALLOWED_TYPES.contains(
                ext.toLowerCase())) {

            throw new InvalidFileTypeException(
                    "Unsupported file format.");
        }
    }
}

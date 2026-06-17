package fa.training.ex4.services.impl;

import fa.training.ex4.dto.request.CreateSubjectRequest;
import fa.training.ex4.dto.request.UpdateSubjectRequest;
import fa.training.ex4.dto.response.DashboardResponse;
import fa.training.ex4.dto.response.SubjectResponse;
import fa.training.ex4.entities.Subject;
import fa.training.ex4.mappers.SubjectMapper;
import fa.training.ex4.repositories.MaterialRepository;
import fa.training.ex4.repositories.SubjectRepository;
import fa.training.ex4.services.SubjectService;
import fa.training.ex4.services.base.GenericSeriveImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SubjectServiceImpl
        extends GenericSeriveImpl<Subject, Long, CreateSubjectRequest, UpdateSubjectRequest, SubjectResponse>
        implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final MaterialRepository materialRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, MaterialRepository materialRepository, SubjectMapper subjectMapper) {
        super(subjectRepository, subjectMapper);
        this.subjectRepository = subjectRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    @Transactional
    public SubjectResponse create(CreateSubjectRequest request) {
        if (subjectRepository.existsBySubjectCode(request.getSubject_code())) {
            throw new IllegalArgumentException("Subject code already exists: " + request.getSubject_code());
        }
        return super.create(request);
    }

    @Override
    @Transactional
    public SubjectResponse update(Long id, UpdateSubjectRequest request) {
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));

        if (!existing.getSubject_code().equals(request.getSubject_code())
                && subjectRepository.existsBySubjectCode(request.getSubject_code())) {
            throw new IllegalArgumentException("Subject code already exists: " + request.getSubject_code());
        }
        return super.update(id, request);
    }

    @Override
    public DashboardResponse getDashboardStats() {
        long totalSubjects = subjectRepository.count();
        long totalMaterials = materialRepository.count();
        Long totalBytes = materialRepository.sumTotalFileSize();

        if (totalBytes == null) totalBytes = 0L;

        return new DashboardResponse(totalSubjects, totalMaterials, formatFileSize(totalBytes));
    }

    private String formatFileSize(long size) {
        if (size <= 0) return "0 Bytes";
        final String[] units = new String[]{"Bytes", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.1f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }

}

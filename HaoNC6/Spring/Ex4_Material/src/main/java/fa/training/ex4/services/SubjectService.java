package fa.training.ex4.services;

import fa.training.ex4.dto.request.CreateSubjectRequest;
import fa.training.ex4.dto.request.UpdateSubjectRequest;
import fa.training.ex4.dto.response.DashboardResponse;
import fa.training.ex4.dto.response.SubjectResponse;
import fa.training.ex4.entities.Subject;
import fa.training.ex4.services.base.GenericService;

import java.util.List;
import java.util.Optional;

public interface SubjectService extends GenericService<Long, CreateSubjectRequest, UpdateSubjectRequest, SubjectResponse> {
    DashboardResponse getDashboardStats();
}

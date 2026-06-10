package fa.training.ex1_lms.controllers.base;

import fa.training.ex1_lms.services.base.GenericService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

public abstract class GenericController<T, ID, S extends GenericService<T, ID>> {

    protected final S service;

    protected GenericController(S service) {
        this.service = service;
    }

    protected abstract String getListView();
    protected abstract String getFormView();
    protected abstract String getListName();
    protected abstract String getEntityName();
    protected abstract String getActivePageList();
    protected abstract String getActivePageForm();
    protected abstract T createEmptyEntity();

    protected String doList(Model model) {
        List<T> list = service.findAll();
        model.addAttribute(getListName(), list);
        model.addAttribute("activePage", getActivePageList());
        return getListView();
    }

    protected String doShowCreateForm(Model model) {
        model.addAttribute(getEntityName(), createEmptyEntity());
        model.addAttribute("isNew", true);
        model.addAttribute("activePage", getActivePageForm());
        return getFormView();
    }

    protected String doSave(T entity, BindingResult bindingResult, Model model) {
        model.addAttribute("activePage", getActivePageForm());

        preSave(entity, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("isNew", true);
            model.addAttribute(getEntityName(), entity);
            return getFormView();
        }

        service.save(entity);
        postSave(entity, model);
        model.addAttribute("isNew", false);
        model.addAttribute(getEntityName(), entity);
        return getFormView();
    }

    protected void doDelete(ID id) {
        service.deleteById(id);
    }

    protected void preSave(T entity, BindingResult bindingResult) {
        // Hook for subclasses
    }

    protected void postSave(T entity, Model model) {
        // Hook for subclasses
    }
}

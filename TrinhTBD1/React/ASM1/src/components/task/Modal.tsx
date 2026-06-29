import React from 'react';
import { Formik, Form, Field, ErrorMessage as FormikError } from 'formik';
import type { Task, TaskStatus } from '../../types/task';
import { Button } from '../common/Button';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (name: string, description: string, status?: TaskStatus) => void;
  initialTask?: Task | null;
}

interface FormValues {
  name: string;
  description: string;
  status: TaskStatus;
}

export const Modal: React.FC<ModalProps> = ({
  isOpen,
  onClose,
  onSubmit,
  initialTask
}) => {
  if (!isOpen) return null;

  const initialValues: FormValues = {
    name: initialTask?.name || '',
    description: initialTask?.description || '',
    status: initialTask?.status || 'Pending'
  };

  const validate = (values: FormValues) => {
    const errors: Partial<Record<keyof FormValues, string>> = {};
    
    const rawName = values?.name ?? '';
    const trimmedName = rawName.trim();

    if (!trimmedName) {
      errors.name = 'Tên công việc là bắt buộc';
    } else if (rawName.length > 40) {
      errors.name = 'Tên công việc tối đa 40 ký tự';
    }

    const rawDesc = values?.description ?? '';
    if (rawDesc && rawDesc.length > 200) {
      errors.description = 'Mô tả tối đa 200 ký tự';
    }

    return errors;
  };

  return (
    <div id="task-modal-backdrop" className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-xs animate-fadeIn">
      <div id="task-modal-container" className="bg-white rounded-xl max-w-lg w-full p-6 shadow-xl border border-slate-100 relative">
        <div className="flex justify-between items-center pb-3 mb-4 border-b border-slate-100">
          <h2 id="modal-title" className="text-lg font-bold text-slate-900">
            {initialTask ? 'Chỉnh sửa công việc' : 'Tạo công việc mới'}
          </h2>
          <button
            type="button"
            id="close-modal-btn"
            aria-label="Đóng cửa sổ"
            onClick={onClose}
            className="text-slate-400 hover:text-slate-600 p-1 rounded-lg hover:bg-slate-100 transition-colors cursor-pointer"
          >
            <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <Formik
          initialValues={initialValues}
          validate={validate}
          onSubmit={(values, { setSubmitting }) => {
            onSubmit(values.name, values.description, values.status);
            setSubmitting(false);
            onClose();
          }}
        >
          {({ isSubmitting, values }) => {
            const nameLen = (values?.name || '').length;
            const descLen = (values?.description || '').length;

            return (
              <Form id="task-form" className="space-y-4">
                <div>
                  <label htmlFor="task-name-input" className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                    Tên công việc <span className="text-red-600">*</span>
                  </label>
                  <Field
                    id="task-name-input"
                    type="text"
                    name="name"
                    data-testid="task-name-input"
                    placeholder="Nhập tên công việc..."
                    className="w-full px-3 py-2 border border-slate-200 rounded-lg text-sm focus:outline-hidden focus:ring-2 focus:ring-red-900 focus:border-red-900"
                  />
                  <div className="flex justify-between items-center mt-1">
                    <FormikError name="name" component="div" id="task-name-error" className="text-xs text-red-600 font-medium" />
                    <span className={`text-xs ml-auto ${nameLen > 40 ? 'text-red-600 font-bold' : 'text-slate-400'}`}>
                      {nameLen}/40
                    </span>
                  </div>
                </div>

                <div>
                  <label htmlFor="task-desc-input" className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                    Mô tả
                  </label>
                  <Field
                    id="task-desc-input"
                    as="textarea"
                    name="description"
                    data-testid="task-desc-input"
                    rows={4}
                    placeholder="Nhập mô tả công việc (không bắt buộc)..."
                    className="w-full px-3 py-2 border border-slate-200 rounded-lg text-sm focus:outline-hidden focus:ring-2 focus:ring-red-900 focus:border-red-900 resize-none"
                  />
                  <div className="flex justify-between items-center mt-1">
                    <FormikError name="description" component="div" id="task-desc-error" className="text-xs text-red-600 font-medium" />
                    <span className={`text-xs ml-auto ${descLen > 200 ? 'text-red-600 font-bold' : 'text-slate-400'}`}>
                      {descLen}/200
                    </span>
                  </div>
                </div>

                <div>
                  <label htmlFor="task-status-select" className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                    Trạng thái
                  </label>
                  <Field
                    id="task-status-select"
                    as="select"
                    name="status"
                    data-testid="task-status-select"
                    className="w-full px-3 py-2 border border-slate-200 rounded-lg text-sm bg-white focus:outline-hidden focus:ring-2 focus:ring-red-900 focus:border-red-900 cursor-pointer"
                  >
                    <option value="Pending">Chờ xử lý</option>
                    <option value="In Progress">Đang thực hiện</option>
                    <option value="Completed">Hoàn thành</option>
                  </Field>
                </div>

                <div className="flex justify-end space-x-2 pt-4 border-t border-slate-100">
                  <Button id="cancel-form-btn" variant="secondary" onClick={onClose}>
                    Hủy
                  </Button>
                  <Button id="submit-form-btn" type="submit" disabled={isSubmitting} data-testid="submit-form-btn">
                    {initialTask ? 'Lưu thay đổi' : 'Tạo công việc'}
                  </Button>
                </div>
              </Form>
            );
          }}
        </Formik>
      </div>
    </div>
  );
};

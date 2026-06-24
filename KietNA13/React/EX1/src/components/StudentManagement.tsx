import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { CrudTable } from './CrudTable';
import { CrudActionBar } from './CrudActionBar';

// 1. Define the Zod Validation Schema
const studentSchema = z.object({
  name: z.string().min(1, 'Tên học sinh không được để trống'),
  age: z.coerce.number().min(1, 'Tuổi phải lớn hơn 0').max(100, 'Tuổi không hợp lệ'),
  grade: z.string().min(1, 'Lớp học không được để trống'),
  status: z.enum(['Active', 'Inactive'], {
    errorMap: () => ({ message: 'Trạng thái không hợp lệ' })
  })
});

// Infer the TypeScript type from the Zod Schema
type StudentFormData = z.infer<typeof studentSchema>;

// Extend the schema type to include ID for our main list
type Student = StudentFormData & { id: string };

const initialStudents: Student[] = [
  { id: '1', name: 'Alice Smith', age: 14, grade: '9th', status: 'Active' },
  { id: '2', name: 'Bob Jones', age: 15, grade: '10th', status: 'Active' },
  { id: '3', name: 'Charlie Brown', age: 16, grade: '11th', status: 'Inactive' },
];

export default function StudentManagement() {
  const [students, setStudents] = useState<Student[]>(initialStudents);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingStudent, setEditingStudent] = useState<Student | null>(null);

  // 2. Initialize React Hook Form with Zod Resolver
  const { register, handleSubmit, reset, formState: { errors } } = useForm<StudentFormData>({
    resolver: zodResolver(studentSchema),
    defaultValues: {
      name: '',
      age: 0,
      grade: '',
      status: 'Active'
    }
  });

  const handleOpenForm = (student?: Student) => {
    if (student) {
      setEditingStudent(student);
      reset({
        name: student.name,
        age: student.age,
        grade: student.grade,
        status: student.status
      });
    } else {
      setEditingStudent(null);
      reset({ name: '', age: 0, grade: '', status: 'Active' });
    }
    setIsModalOpen(true);
  };

  const handleCloseForm = () => {
    setIsModalOpen(false);
    setEditingStudent(null);
    reset(); // Clear form when closing
  };

  // 3. Form Submit Handler (only called if validation passes)
  const onSubmit = (data: StudentFormData) => {
    if (editingStudent) {
      setStudents(students.map(s => s.id === editingStudent.id ? { ...data, id: editingStudent.id } : s));
    } else {
      const nextId = students.length > 0 
        ? String(Math.max(...students.map(s => parseInt(s.id) || 0)) + 1)
        : '1';
      const newStudent: Student = {
        ...data,
        id: nextId
      };
      setStudents([...students, newStudent]);
    }
    handleCloseForm();
  };

  const handleDelete = (student: Student) => {
    if (window.confirm(`Are you sure you want to delete ${student.name}?`)) {
      setStudents(students.filter(s => s.id !== student.id));
    }
  };

  return (
    <div className="container">
      <h1>Student Management</h1>
      
      <CrudActionBar
        onAdd={() => handleOpenForm()}
      />

      <CrudTable
        data={students}
        onEdit={(s) => handleOpenForm(s)}
        onDelete={(s) => handleDelete(s)}
      />

      {isModalOpen && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>{editingStudent ? 'Chỉnh sửa Học sinh' : 'Thêm Học sinh mới'}</h2>
            
            {/* React Hook Form HandleSubmit */}
            <form onSubmit={handleSubmit(onSubmit)}>
              <div className="form-group">
                <label>Họ và Tên</label>
                <input 
                  type="text" 
                  {...register('name')}
                />
                {errors.name && <span className="error-message">{errors.name.message}</span>}
              </div>

              <div className="form-group">
                <label>Tuổi</label>
                <input 
                  type="number" 
                  {...register('age')}
                />
                {errors.age && <span className="error-message">{errors.age.message}</span>}
              </div>

              <div className="form-group">
                <label>Lớp</label>
                <input 
                  type="text" 
                  {...register('grade')}
                />
                {errors.grade && <span className="error-message">{errors.grade.message}</span>}
              </div>

              <div className="form-group">
                <label>Trạng thái (Status)</label>
                {/* 4. Swapped Input to Select Dropdown */}
                <select {...register('status')}>
                  <option value="Active">Active</option>
                  <option value="Inactive">Inactive</option>
                </select>
                {errors.status && <span className="error-message">{errors.status.message}</span>}
              </div>

              <div className="modal-actions">
                <button type="button" className="btn btn-outline" onClick={handleCloseForm}>Hủy</button>
                <button type="submit" className="btn btn-success">Lưu lại</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

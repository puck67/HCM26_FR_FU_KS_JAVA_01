import { useState } from "react";
import { CrudTable } from "../components/CrudTable";
import { Modal } from "../components/Modal";
import { Button } from "../components/Button";
import { SearchOutlined } from '@ant-design/icons';
import { useToast } from "../components/ToastContext";

interface Student {
  id: string;
  name: string;
  grade: string;
  gpa: number;
}

export default function StudentPage() {
  const { addToast } = useToast();
  const [data, setData] = useState<Student[]>([
    { id: "STU01", name: "John Doe", grade: "10A1", gpa: 8.5 },
    { id: "STU02", name: "Jane Smith", grade: "10A2", gpa: 9.0 }
  ]);
  
  const [modalState, setModalState] = useState<{type: 'add' | 'edit' | 'delete' | null, student: Partial<Student> | null}>({ type: null, student: null });
  const [search, setSearch] = useState("");

  const handleEdit = (student: Student) => setModalState({ type: 'edit', student });
  const handleDelete = (student: Student) => setModalState({ type: 'delete', student });

  const handleConfirm = () => {
    if (modalState.type === 'delete' && modalState.student?.id) {
      setData(data.filter(s => s.id !== modalState.student?.id));
      addToast(`Deleted student: ${modalState.student.name}`, 'success');
    } else if (modalState.type === 'edit' && modalState.student?.id) {
      setData(data.map(s => s.id === modalState.student?.id ? modalState.student as Student : s));
      addToast(`Updated student: ${modalState.student.name}`, 'success');
    } else if (modalState.type === 'add') {
      const newStudent = { ...modalState.student, id: Date.now().toString() } as Student;
      setData([...data, newStudent]);
      addToast(`Added new student: ${newStudent.name}`, 'success');
    }
    setModalState({ type: null, student: null });
  };

  const handleExport = () => {
    addToast('Student records exported to PDF.', 'info');
  };

  const filteredData = data.filter(s => s.name.toLowerCase().includes(search.toLowerCase()) || s.id.toLowerCase().includes(search.toLowerCase()));

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h2>Student Records</h2>
        <div style={{ position: 'relative' }}>
          <SearchOutlined style={{ position: 'absolute', left: '16px', top: '16px', color: '#94A3B8' }} />
          <input 
            type="text" 
            placeholder="Search students..." 
            className="search-bar"
            style={{ paddingLeft: '40px' }}
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <div>
          <Button title="Export" action={handleExport} style="btn btn-ghost" />
          <Button title="Add Student" action={() => setModalState({ type: 'add', student: {} })} />
        </div>
      </div>
      
      <CrudTable 
        data={filteredData} 
        onEdit={handleEdit}
        onDelete={handleDelete}
      />
      
      <Modal 
        isOpen={modalState.type !== null} 
        title={modalState.type === 'add' ? "Add Student" : modalState.type === 'edit' ? "Edit Student" : "Confirm Deletion"}
        onClose={() => setModalState({ type: null, student: null })}
        onConfirm={handleConfirm}
        confirmText={modalState.type === 'delete' ? "Delete" : "Save"}
        type={modalState.type === 'delete' ? "danger" : "primary"}
      >
        {modalState.type === 'delete' ? (
          <p>Are you sure you want to delete student <strong>{modalState.student?.name}</strong>?</p>
        ) : (
          <>
            <label>Student ID</label>
            <input 
              value={modalState.student?.id || ''} 
              onChange={e => setModalState({...modalState, student: {...modalState.student, id: e.target.value}})} 
              disabled={modalState.type === 'edit'}
            />
            <label>Full Name</label>
            <input 
              value={modalState.student?.name || ''} 
              onChange={e => setModalState({...modalState, student: {...modalState.student, name: e.target.value}})} 
            />
            <label>Class / Grade</label>
            <input 
              value={modalState.student?.grade || ''} 
              onChange={e => setModalState({...modalState, student: {...modalState.student, grade: e.target.value}})} 
            />
            <label>GPA</label>
            <input 
              type="number"
              value={modalState.student?.gpa || 0} 
              onChange={e => setModalState({...modalState, student: {...modalState.student, gpa: parseFloat(e.target.value)}})} 
            />
          </>
        )}
      </Modal>
    </div>
  );
}

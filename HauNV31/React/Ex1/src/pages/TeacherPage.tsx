import { useState } from "react";
import { CrudTable } from "../components/CrudTable";
import { Modal } from "../components/Modal";
import { Button } from "../components/Button";
import { SearchOutlined } from '@ant-design/icons';
import { useToast } from "../components/ToastContext";

type ScheduleItem = {
  day: string;
  time: string;
  classRoom: string;
};

interface Teacher {
  id: string;
  name: string;
  subject: string;
  experienceYears: number;
  schedule?: ScheduleItem[];
}

export default function TeacherPage() {
  const { addToast } = useToast();
  const [data, setData] = useState<Teacher[]>([
    { 
      id: "1", name: "David Johnson", subject: "Math", experienceYears: 5,
      schedule: [
        { day: "Monday", time: "07:00 - 09:00", classRoom: "Room 101" },
        { day: "Wednesday", time: "09:30 - 11:30", classRoom: "Room 102" }
      ]
    },
    { 
      id: "2", name: "Emily Clark", subject: "Literature", experienceYears: 10,
      schedule: [
        { day: "Tuesday", time: "13:00 - 15:00", classRoom: "Room 201" },
        { day: "Friday", time: "07:00 - 09:00", classRoom: "Room 205" }
      ]
    }
  ]);
  
  const [modalState, setModalState] = useState<{type: 'add' | 'edit' | 'delete' | 'schedule' | null, teacher: Partial<Teacher> | null}>({ type: null, teacher: null });
  const [search, setSearch] = useState("");

  const handleEdit = (teacher: Teacher) => setModalState({ type: 'edit', teacher });
  const handleDelete = (teacher: Teacher) => setModalState({ type: 'delete', teacher });
  const handleSchedule = (teacher: Teacher) => setModalState({ type: 'schedule', teacher });

  const handleConfirm = () => {
    if (modalState.type === 'delete' && modalState.teacher?.id) {
      setData(data.filter(t => t.id !== modalState.teacher?.id));
      addToast(`Deleted teacher: ${modalState.teacher.name}`, 'success');
    } else if (modalState.type === 'edit' && modalState.teacher?.id) {
      setData(data.map(t => t.id === modalState.teacher?.id ? modalState.teacher as Teacher : t));
      addToast(`Updated teacher: ${modalState.teacher.name}`, 'success');
    } else if (modalState.type === 'add') {
      const newTeacher = { ...modalState.teacher, id: Date.now().toString(), schedule: [] } as Teacher;
      setData([...data, newTeacher]);
      addToast(`Added new teacher: ${newTeacher.name}`, 'success');
    }
    setModalState({ type: null, teacher: null });
  };

  const filteredData = data.filter(t => t.name.toLowerCase().includes(search.toLowerCase()) || t.subject.toLowerCase().includes(search.toLowerCase()));

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h2>Teacher Roster</h2>
        <div style={{ position: 'relative' }}>
          <SearchOutlined style={{ position: 'absolute', left: '16px', top: '16px', color: '#94A3B8' }} />
          <input 
            type="text" 
            placeholder="Search teachers..." 
            className="search-bar"
            style={{ paddingLeft: '40px' }}
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <Button title="Add Teacher" action={() => setModalState({ type: 'add', teacher: {} })} />
      </div>
      
      <CrudTable 
        data={filteredData} 
        onEdit={handleEdit}
        onDelete={handleDelete}
        extraActions={(teacher) => [
          { title: "View Schedule", action: () => handleSchedule(teacher), style: "btn btn-ghost" }
        ]}
      />
      <Modal 
        isOpen={modalState.type !== null} 
        title={
          modalState.type === 'add' ? "Add Teacher" : 
          modalState.type === 'edit' ? "Edit Teacher" : 
          modalState.type === 'schedule' ? `${modalState.teacher?.name}'s Schedule` :
          "Confirm Deletion"
        }
        onClose={() => setModalState({ type: null, teacher: null })}
        onConfirm={modalState.type === 'schedule' ? () => setModalState({ type: null, teacher: null }) : handleConfirm}
        confirmText={modalState.type === 'delete' ? "Delete" : modalState.type === 'schedule' ? "Close" : "Save"}
        type={modalState.type === 'delete' ? "danger" : "primary"}
        showCancel={modalState.type !== 'schedule'}
      >
        {modalState.type === 'delete' ? (
          <p>Are you sure you want to delete teacher <strong>{modalState.teacher?.name}</strong>?</p>
        ) : modalState.type === 'schedule' ? (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            {modalState.teacher?.schedule?.length ? (
              modalState.teacher.schedule.map((item, idx) => (
                <div key={idx} style={{ background: 'rgba(255,255,255,0.05)', padding: '12px', borderRadius: '8px', border: '1px solid rgba(255,255,255,0.1)' }}>
                  <strong style={{ color: '#0166FF' }}>{item.day}</strong>: {item.time} ({item.classRoom})
                </div>
              ))
            ) : (
              <p>This teacher has no schedule.</p>
            )}
          </div>
        ) : (
          <>
            <label>Full Name</label>
            <input 
              value={modalState.teacher?.name || ''} 
              onChange={e => setModalState({...modalState, teacher: {...modalState.teacher, name: e.target.value}})} 
            />
            <label>Subject</label>
            <input 
              value={modalState.teacher?.subject || ''} 
              onChange={e => setModalState({...modalState, teacher: {...modalState.teacher, subject: e.target.value}})} 
            />
            <label>Years of Experience</label>
            <input 
              type="number"
              value={modalState.teacher?.experienceYears || 0} 
              onChange={e => setModalState({...modalState, teacher: {...modalState.teacher, experienceYears: parseInt(e.target.value)}})} 
            />
          </>
        )}
      </Modal>
    </div>
  );
}

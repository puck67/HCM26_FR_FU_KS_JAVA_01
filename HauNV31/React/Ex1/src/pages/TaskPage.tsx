import { useState } from "react";
import { Modal } from "../components/Modal";
import { Button } from "../components/Button";
import { useToast } from "../components/ToastContext";

type TaskStatus = "Pending" | "In Progress" | "Done";

interface Task {
  id: string;
  title: string;
  status: TaskStatus;
}

export default function TaskPage() {
  const { addToast } = useToast();
  const [data, setData] = useState<Task[]>([
    { id: "1", title: "Complete UI Report", status: "Pending" },
    { id: "2", title: "Design Team Meeting", status: "Done" },
    { id: "3", title: "API Integration", status: "In Progress" },
  ]);
  
  const [modalState, setModalState] = useState<{type: 'add' | 'edit' | 'delete' | null, task: Partial<Task> | null}>({ type: null, task: null });
  const [draggedTaskId, setDraggedTaskId] = useState<string | null>(null);

  const handleDelete = (task: Task) => setModalState({ type: 'delete', task });
  const handleEdit = (task: Task) => setModalState({ type: 'edit', task });

  const handleConfirm = () => {
    if (modalState.type === 'delete' && modalState.task?.id) {
      setData(data.filter(t => t.id !== modalState.task?.id));
      addToast(`Deleted task: ${modalState.task.title}`, 'success');
    } else if (modalState.type === 'edit' && modalState.task?.id) {
      setData(data.map(t => t.id === modalState.task?.id ? modalState.task as Task : t));
      addToast(`Updated task: ${modalState.task.title}`, 'success');
    } else if (modalState.type === 'add') {
      const newTask = { ...modalState.task, id: Date.now().toString(), status: modalState.task?.status || 'Pending' } as Task;
      setData([...data, newTask]);
      addToast(`Added new task: ${newTask.title}`, 'success');
    }
    setModalState({ type: null, task: null });
  };

  const handleDragStart = (e: React.DragEvent, id: string) => {
    setDraggedTaskId(id);
    e.dataTransfer.effectAllowed = "move";
  };

  const handleDrop = (e: React.DragEvent, status: TaskStatus) => {
    e.preventDefault();
    if (draggedTaskId) {
      const task = data.find(t => t.id === draggedTaskId);
      if (task && task.status !== status) {
        setData(data.map(t => t.id === draggedTaskId ? { ...t, status } : t));
        addToast(`Moved task to ${status}`, 'info');
      }
    }
    setDraggedTaskId(null);
  };

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
  };

  const renderColumn = (status: TaskStatus) => {
    const tasks = data.filter(t => t.status === status);
    return (
      <div 
        className="kanban-column"
        onDrop={(e) => handleDrop(e, status)}
        onDragOver={handleDragOver}
      >
        <h3 className="kanban-header">{status} ({tasks.length})</h3>
        <div className="kanban-cards">
          {tasks.map(task => (
            <div 
              key={task.id} 
              className="kanban-card"
              draggable
              onDragStart={(e) => handleDragStart(e, task.id)}
            >
              <h4>{task.title}</h4>
              <div className="kanban-actions">
                <Button title="Edit" action={() => handleEdit(task)} />
                <Button title="Delete" action={() => handleDelete(task)} />
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', flex: 1, overflow: 'hidden' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px', flexShrink: 0 }}>
        <h2>Task Management</h2>
        <Button title="Add Task" action={() => setModalState({ type: 'add', task: { status: 'Pending' } })} />
      </div>

      <div className="kanban-board">
        {renderColumn("Pending")}
        {renderColumn("In Progress")}
        {renderColumn("Done")}
      </div>

      <Modal 
        isOpen={modalState.type !== null} 
        title={modalState.type === 'add' ? "Add Task" : modalState.type === 'edit' ? "Edit Task" : "Confirm Deletion"}
        onClose={() => setModalState({ type: null, task: null })}
        onConfirm={handleConfirm}
        confirmText={modalState.type === 'delete' ? "Delete" : "Save"}
        type={modalState.type === 'delete' ? "danger" : "primary"}
      >
        {modalState.type === 'delete' ? (
          <p>Are you sure you want to delete task <strong>{modalState.task?.title}</strong>?</p>
        ) : (
          <>
            <label>Title</label>
            <input 
              value={modalState.task?.title || ''} 
              onChange={e => setModalState({...modalState, task: {...modalState.task, title: e.target.value}})} 
            />
            <label>Status (Pending/In Progress/Done)</label>
            <input 
              value={modalState.task?.status || ''} 
              onChange={e => setModalState({...modalState, task: {...modalState.task, status: e.target.value as any}})} 
            />
          </>
        )}
      </Modal>
    </div>
  );
}

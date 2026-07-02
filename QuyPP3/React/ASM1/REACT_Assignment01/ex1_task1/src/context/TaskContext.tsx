import React, { createContext, useContext, useReducer, useEffect, useState } from "react";

export interface Task {
  id: string;
  name: string;
  description: string;
}

type State = {
  tasks: Task[];
  loading: boolean;
  error: string | null;
};

type Action =
  | { type: "FETCH_START" }
  | { type: "FETCH_SUCCESS"; payload: Task[] }
  | { type: "FETCH_ERROR"; payload: string }
  | { type: "ADD_TASK"; payload: Task }
  | { type: "UPDATE_TASK"; payload: Task }
  | { type: "DELETE_TASK"; payload: string };

const initialState: State = {
  tasks: [],
  loading: false,
  error: null,
};

function taskReducer(state: State, action: Action): State {
  switch (action.type) {
    case "FETCH_START":
      return { ...state, loading: true, error: null };
    case "FETCH_SUCCESS":
      return { ...state, loading: false, tasks: action.payload };
    case "FETCH_ERROR":
      return { ...state, loading: false, error: action.payload };
    case "ADD_TASK":
      return { ...state, tasks: [...state.tasks, action.payload] };
    case "UPDATE_TASK":
      return {
        ...state,
        tasks: state.tasks.map((t) => (t.id === action.payload.id ? action.payload : t)),
      };
    case "DELETE_TASK":
      return { ...state, tasks: state.tasks.filter((t) => t.id !== action.payload) };
    default:
      return state;
  }
}

const TaskContext = createContext<{
  state: State;
  isFormOpen: boolean;
  setIsFormOpen: (open: boolean) => void;
  selectedTask: Task | null;
  setSelectedTask: (task: Task | null) => void;
  dispatch: React.Dispatch<Action>;
} | null>(null);

export function TaskProvider({ children }: { children: React.ReactNode }) {
  const [state, dispatch] = useReducer(taskReducer, initialState);
  
  // useState utilized specifically for layout / UI workflow toggles
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedTask, setSelectedTask] = useState<Task | null>(null);

  // Simulating REST API Data Fetch using a reliable mock payload
  useEffect(() => {
    dispatch({ type: "FETCH_START" });
    setTimeout(() => {
      try {
        const mockTasks: Task[] = [
          { id: "1", name: "Configure Tailwind CSS", description: "Set up tailwind.config.js and utility layers." },
          { id: "2", name: "Implement Formik Forms", description: "Create declarative forms with custom validation rules." },
        ];
        dispatch({ type: "FETCH_SUCCESS", payload: mockTasks });
      } catch (err) {
        dispatch({ type: "FETCH_ERROR", payload: "Failed to load dashboard data." });
      }
    }, 800);
  }, []);

  return (
    <TaskContext.Provider value={{ state, isFormOpen, setIsFormOpen, selectedTask, setSelectedTask, dispatch }}>
      {children}
    </TaskContext.Provider>
  );
}

export const useTasks = () => {
  const context = useContext(TaskContext);
  if (!context) throw new Error("useTasks must be used within a TaskProvider");
  return context;
};

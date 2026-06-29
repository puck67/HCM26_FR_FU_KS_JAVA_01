export interface Task {
  id: string;
  name: string;
  description: string;
  status: "Chờ xử lý" | "Đang thực hiện" | "Hoàn thành";
  priority: "Thấp" | "Trung bình" | "Cao";
}

export type TaskAction =
  | { type: "SET_TASKS"; payload: Task[] }
  | { type: "ADD_TASK"; payload: Task }
  | { type: "UPDATE_TASK"; payload: Task }
  | { type: "DELETE_TASK"; payload: string };

export function taskReducer(state: Task[], action: TaskAction): Task[] {
  switch (action.type) {
    case "SET_TASKS":
      return action.payload;
    case "ADD_TASK":
      return [action.payload, ...state];
    case "UPDATE_TASK":
      return state.map((t) => (t.id === action.payload.id ? action.payload : t));
    case "DELETE_TASK":
      return state.filter((t) => t.id !== action.payload);
    default:
      return state;
  }
}

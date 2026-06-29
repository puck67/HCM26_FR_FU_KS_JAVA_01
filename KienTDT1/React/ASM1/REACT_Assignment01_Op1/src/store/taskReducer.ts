import type { Task } from "../types/task";

export type Action =
  | { type: "SET"; payload: Task[] }
  | { type: "ADD"; payload: Task }
  | { type: "UPDATE"; payload: Task }
  | { type: "DELETE"; payload: number };

export const taskReducer = (state: Task[], action: Action): Task[] => {
  switch (action.type) {
    case "SET":
      return action.payload;
    case "ADD":
      return [...state, action.payload];
    case "UPDATE":
      return state.map(t => (t.id === action.payload.id ? action.payload : t));
    case "DELETE":
      return state.filter(t => t.id !== action.payload);
    default:
      return state;
  }
};
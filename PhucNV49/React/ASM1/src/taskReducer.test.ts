import { taskReducer, type Task } from "./taskReducer";

// ponytail: assert-based self-check, no test framework. Run: npx tsx src/taskReducer.test.ts
const sample: Task = {
  id: "1", name: "A", description: "", status: "todo", createdAt: "2025-01-01T00:00:00Z",
};

// SET_TASKS replaces state
let s = taskReducer([], { type: "SET_TASKS", payload: [sample] });
console.assert(s.length === 1 && s[0].id === "1", "SET_TASKS failed");

// ADD_TASK prepends
const sample2: Task = { ...sample, id: "2", name: "B" };
s = taskReducer([sample], { type: "ADD_TASK", payload: sample2 });
console.assert(s.length === 2 && s[0].id === "2", "ADD_TASK should prepend");

// UPDATE_TASK replaces matching id
s = taskReducer([sample], { type: "UPDATE_TASK", payload: { ...sample, name: "Updated" } });
console.assert(s[0].name === "Updated", "UPDATE_TASK failed");

// DELETE_TASK removes by id
s = taskReducer([sample, sample2], { type: "DELETE_TASK", payload: "1" });
console.assert(s.length === 1 && s[0].id === "2", "DELETE_TASK failed");

console.log("All taskReducer tests passed");

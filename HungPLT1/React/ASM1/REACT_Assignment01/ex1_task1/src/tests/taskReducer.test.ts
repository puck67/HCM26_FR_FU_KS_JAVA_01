import { describe, it, expect } from 'vitest';
import { taskReducer } from '../context/TaskContext';
import type { TaskState, TaskAction } from '../context/TaskContext';
import type { Task } from '../types';

describe('taskReducer', () => {
  const initialState: TaskState = {
    tasks: [],
    loading: false,
    error: null,
  };

  const sampleTask: Task = {
    id: '1',
    name: 'Test Task',
    description: 'Test Description',
    status: 'pending',
    createdAt: new Date().toISOString(),
  };

  it('should handle FETCH_INIT', () => {
    const action: TaskAction = { type: 'FETCH_INIT' };
    const state = taskReducer({ ...initialState, error: 'Previous error' }, action);
    expect(state.loading).toBe(true);
    expect(state.error).toBeNull();
  });

  it('should handle FETCH_SUCCESS', () => {
    const action: TaskAction = { type: 'FETCH_SUCCESS', payload: [sampleTask] };
    const state = taskReducer({ ...initialState, loading: true }, action);
    expect(state.loading).toBe(false);
    expect(state.tasks).toEqual([sampleTask]);
    expect(state.error).toBeNull();
  });

  it('should handle FETCH_FAILURE', () => {
    const action: TaskAction = { type: 'FETCH_FAILURE', payload: 'Fetch failed' };
    const state = taskReducer({ ...initialState, loading: true }, action);
    expect(state.loading).toBe(false);
    expect(state.error).toBe('Fetch failed');
  });

  it('should handle ADD_TASK', () => {
    const action: TaskAction = { type: 'ADD_TASK', payload: sampleTask };
    const state = taskReducer(initialState, action);
    expect(state.tasks).toEqual([sampleTask]);
  });

  it('should handle UPDATE_TASK', () => {
    const activeState: TaskState = {
      tasks: [sampleTask],
      loading: false,
      error: null,
    };
    const updatedTask: Task = { ...sampleTask, name: 'Updated Name', status: 'completed' };
    const action: TaskAction = { type: 'UPDATE_TASK', payload: updatedTask };
    const state = taskReducer(activeState, action);
    expect(state.tasks[0].name).toBe('Updated Name');
    expect(state.tasks[0].status).toBe('completed');
  });

  it('should handle DELETE_TASK', () => {
    const activeState: TaskState = {
      tasks: [sampleTask],
      loading: false,
      error: null,
    };
    const action: TaskAction = { type: 'DELETE_TASK', payload: '1' };
    const state = taskReducer(activeState, action);
    expect(state.tasks).toEqual([]);
  });
});

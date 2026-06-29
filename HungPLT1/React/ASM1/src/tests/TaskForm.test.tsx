import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { TaskForm } from '../components/TaskForm';

describe('TaskForm Validation', () => {
  it('shows validation error when task name is empty and submitted', async () => {
    const onSubmit = vi.fn();
    const onCancel = vi.fn();

    render(
      <TaskForm
        onSubmit={onSubmit}
        onCancel={onCancel}
        submitButtonText="Submit Task"
      />
    );

    const submitBtn = screen.getByRole('button', { name: 'Submit Task' });
    fireEvent.click(submitBtn);

    const errorMsg = await screen.findByText('Task name is required');
    expect(errorMsg).toBeInTheDocument();
    expect(onSubmit).not.toHaveBeenCalled();
  });

  it('shows validation error when task name exceeds 40 characters', async () => {
    const onSubmit = vi.fn();
    const onCancel = vi.fn();

    render(
      <TaskForm
        onSubmit={onSubmit}
        onCancel={onCancel}
        submitButtonText="Submit Task"
      />
    );

    const nameInput = screen.getByPlaceholderText('e.g. Design homepage layout');
    fireEvent.change(nameInput, { target: { value: 'a'.repeat(41) } });

    const submitBtn = screen.getByRole('button', { name: 'Submit Task' });
    fireEvent.click(submitBtn);

    const errorMsg = await screen.findByText('Task name cannot exceed 40 characters');
    expect(errorMsg).toBeInTheDocument();
    expect(onSubmit).not.toHaveBeenCalled();
  });

  it('shows validation error when description exceeds 200 characters', async () => {
    const onSubmit = vi.fn();
    const onCancel = vi.fn();

    render(
      <TaskForm
        onSubmit={onSubmit}
        onCancel={onCancel}
        submitButtonText="Submit Task"
      />
    );

    const nameInput = screen.getByPlaceholderText('e.g. Design homepage layout');
    fireEvent.change(nameInput, { target: { value: 'Valid Task Name' } });

    const descInput = screen.getByPlaceholderText('Describe the task objective, criteria for success...');
    fireEvent.change(descInput, { target: { value: 'a'.repeat(201) } });

    const submitBtn = screen.getByRole('button', { name: 'Submit Task' });
    fireEvent.click(submitBtn);

    const errorMsg = await screen.findByText('Description cannot exceed 200 characters');
    expect(errorMsg).toBeInTheDocument();
    expect(onSubmit).not.toHaveBeenCalled();
  });

  it('submits form successfully when values are valid', async () => {
    const onSubmit = vi.fn().mockResolvedValue(undefined);
    const onCancel = vi.fn();

    render(
      <TaskForm
        onSubmit={onSubmit}
        onCancel={onCancel}
        submitButtonText="Submit Task"
      />
    );

    const nameInput = screen.getByPlaceholderText('e.g. Design homepage layout');
    fireEvent.change(nameInput, { target: { value: 'Learn React Router' } });

    const descInput = screen.getByPlaceholderText('Describe the task objective, criteria for success...');
    fireEvent.change(descInput, { target: { value: 'Understand nested routing and route protection.' } });

    const statusSelect = screen.getByLabelText('Status');
    fireEvent.change(statusSelect, { target: { value: 'in_progress' } });

    const submitBtn = screen.getByRole('button', { name: 'Submit Task' });
    fireEvent.click(submitBtn);

    await waitFor(() => {
      expect(onSubmit).toHaveBeenCalledWith({
        name: 'Learn React Router',
        description: 'Understand nested routing and route protection.',
        status: 'in_progress',
      });
    });
  });

  it('calls onCancel handler when Cancel button is clicked', () => {
    const onSubmit = vi.fn();
    const onCancel = vi.fn();

    render(
      <TaskForm
        onSubmit={onSubmit}
        onCancel={onCancel}
        submitButtonText="Submit Task"
      />
    );

    const cancelBtn = screen.getByRole('button', { name: 'Cancel' });
    fireEvent.click(cancelBtn);

    expect(onCancel).toHaveBeenCalled();
  });
});

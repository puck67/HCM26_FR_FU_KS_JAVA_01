import { getTasks } from '@/lib/data';
import Link from 'next/link';
import { deleteTask, toggleTaskCompleted } from '@/app/actions';

// Ensure this page is not statically cached since it depends on our in-memory data
export const dynamic = 'force-dynamic';

export default async function TasksPage() {
  const tasks = await getTasks();

  return (
    <main className="min-h-screen p-10 bg-gray-100">
      <div className="max-w-4xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold text-gray-800">Your Tasks</h1>
          <Link 
            href="/tasks/new" 
            className="px-4 py-2 bg-indigo-600 text-white rounded shadow hover:bg-indigo-700 transition"
          >
            + Create New Task
          </Link>
        </div>

        {tasks.length === 0 ? (
          <p className="text-gray-500 text-center py-10 bg-white rounded-lg shadow">No tasks found. Create one!</p>
        ) : (
          <ul className="space-y-4">
            {tasks.map(task => {
              const deleteAction = deleteTask.bind(null, task.id);
              const toggleAction = toggleTaskCompleted.bind(null, task.id);

              return (
                <li 
                  key={task.id} 
                  className={`p-6 rounded-xl shadow flex justify-between items-center transition ${task.completed ? 'bg-green-50 opacity-80' : 'bg-white'}`}
                >
                  <div className="flex-1">
                    <h2 className={`text-xl font-semibold mb-1 ${task.completed ? 'line-through text-gray-500' : 'text-gray-800'}`}>
                      {task.name}
                    </h2>
                    {task.description && (
                      <p className={`text-sm ${task.completed ? 'text-gray-400' : 'text-gray-600'}`}>
                        {task.description}
                      </p>
                    )}
                  </div>
                  <div className="flex gap-2 ml-4">
                    <form action={toggleAction}>
                      <button 
                        type="submit" 
                        className={`px-3 py-1 text-sm rounded border transition ${task.completed ? 'border-gray-400 text-gray-600 hover:bg-gray-200' : 'border-green-500 text-green-600 hover:bg-green-50'}`}
                      >
                        {task.completed ? 'Undo' : 'Complete'}
                      </button>
                    </form>
                    <Link 
                      href={`/tasks/${task.id}`}
                      className="px-3 py-1 text-sm rounded border border-blue-500 text-blue-600 hover:bg-blue-50 transition block"
                    >
                      Edit
                    </Link>
                    <form action={deleteAction}>
                      <button 
                        type="submit" 
                        className="px-3 py-1 text-sm rounded border border-red-500 text-red-600 hover:bg-red-50 transition"
                      >
                        Delete
                      </button>
                    </form>
                  </div>
                </li>
              );
            })}
          </ul>
        )}
      </div>
    </main>
  );
}

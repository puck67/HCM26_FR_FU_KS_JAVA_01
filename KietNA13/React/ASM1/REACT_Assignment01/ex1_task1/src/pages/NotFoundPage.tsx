import { Link } from 'react-router-dom';
import { ROUTES } from '../constants/routes';

const NotFoundPage = () => (
  <main className="flex min-h-[70vh] flex-col items-center justify-center px-4 text-center">
    <p
      className="text-8xl font-bold text-indigo-200"
      aria-hidden="true"
    >
      404
    </p>
    <h1 className="mt-4 text-2xl font-semibold text-gray-900">
      Page not found
    </h1>
    <p className="mt-2 max-w-sm text-sm text-gray-500">
      The page you're looking for doesn't exist or has been moved.
    </p>
    <Link
      to={ROUTES.HOME}
      className="mt-6 rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white hover:bg-indigo-700 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-indigo-500 focus-visible:ring-offset-2"
    >
      Go back home
    </Link>
  </main>
);

export default NotFoundPage;

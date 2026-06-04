<?php
header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
  http_response_code(405);
  echo json_encode(['success' => false, 'message' => 'Method not allowed.']);
  exit;
}

$alias = isset($_POST['alias']) ? trim((string) $_POST['alias']) : '';
$password = isset($_POST['password']) ? trim((string) $_POST['password']) : '';
$errors = [];

if ($alias === '') {
  $errors[] = 'Alias is required.';
}
if ($password === '') {
  $errors[] = 'Password is required.';
}

if (count($errors) > 0) {
  http_response_code(422);
  echo json_encode([
    'success' => false,
    'message' => 'Login validation failed.',
    'errors' => $errors
  ]);
  exit;
}

echo json_encode([
  'success' => true,
  'message' => 'Login successful.'
]);

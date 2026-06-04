<?php
header('Content-Type: application/json; charset=utf-8');

$id = null;
if (isset($_REQUEST['id'])) {
  $id = trim((string) $_REQUEST['id']);
}

if ($id === null || $id === '') {
  http_response_code(400);
  echo json_encode([
    'success' => false,
    'message' => 'Poll id is required.'
  ]);
  exit;
}

echo json_encode([
  'success' => true,
  'message' => 'Poll #' . htmlspecialchars($id, ENT_QUOTES, 'UTF-8') . ' deleted successfully.',
  'id' => $id
]);

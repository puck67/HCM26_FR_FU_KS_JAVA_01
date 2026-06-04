<?php
header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
  http_response_code(405);
  echo json_encode(['success' => false, 'message' => 'Method not allowed.']);
  exit;
}

$errors = [];
$pollName = isset($_POST['pollName']) ? trim((string) $_POST['pollName']) : '';

if ($pollName === '') {
  $errors[] = 'Name poll is required.';
} elseif (strlen($pollName) < 3) {
  $errors[] = 'Name poll must be at least 3 characters.';
} elseif (strlen($pollName) > 255) {
  $errors[] = 'Name poll must not exceed 255 characters.';
}

$questions = isset($_POST['questions']) && is_array($_POST['questions']) ? $_POST['questions'] : [];

if (count($questions) === 0) {
  $errors[] = 'At least one question is required.';
}

foreach ($questions as $index => $question) {
  $num = $index + 1;
  $text = isset($question['text']) ? trim((string) $question['text']) : '';

  if ($text === '') {
    $errors[] = 'Your question (block ' . $num . ') is required.';
  } elseif (strlen($text) < 3) {
    $errors[] = 'Your question (block ' . $num . ') must be at least 3 characters.';
  } elseif (strlen($text) > 255) {
    $errors[] = 'Your question (block ' . $num . ') must not exceed 255 characters.';
  }

  $answers = isset($question['answers']) && is_array($question['answers']) ? $question['answers'] : [];
  $filled = array_filter(array_map('trim', $answers));

  if (count($filled) === 0) {
    $errors[] = 'Question ' . $num . ' must have at least one answer.';
    continue;
  }

  foreach ($filled as $aIndex => $answer) {
    if (strlen($answer) < 3) {
      $errors[] = 'Answer ' . ($aIndex + 1) . ' of question ' . $num . ' must be at least 3 characters.';
    } elseif (strlen($answer) > 200) {
      $errors[] = 'Answer ' . ($aIndex + 1) . ' of question ' . $num . ' must not exceed 200 characters.';
    }
  }
}

if (count($errors) > 0) {
  http_response_code(422);
  echo json_encode([
    'success' => false,
    'message' => 'Server-side validation failed.',
    'errors' => $errors
  ]);
  exit;
}

echo json_encode([
  'success' => true,
  'message' => 'The form was completed successfully. Server-side validation passed.'
]);

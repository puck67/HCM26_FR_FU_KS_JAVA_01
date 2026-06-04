const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = process.env.PORT || 3000;

const MIME_TYPES = {
  '.html': 'text/html',
  '.css': 'text/css',
  '.js': 'text/javascript',
  '.json': 'application/json',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.gif': 'image/gif',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon'
};

const server = http.createServer((req, res) => {
  console.log(`${req.method} ${req.url}`);
  
  // Clean query parameters out of the file path
  const parsedUrl = req.url.split('?')[0];

  // API Route: Delete Poll
  if (req.method === 'POST' && parsedUrl === '/api/delete-poll') {
    let body = '';
    req.on('data', chunk => {
      body += chunk.toString();
    });
    req.on('end', () => {
      try {
        if (!body) {
          res.writeHead(400, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ success: false, errors: ['Request body is empty or null.'] }));
          return;
        }

        const data = JSON.parse(body);
        if (!data || typeof data !== 'object' || data.id === undefined || data.id === null) {
          res.writeHead(400, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ success: false, errors: ['Missing target Poll ID for deletion.'] }));
          return;
        }

        const idNum = Number(data.id);
        if (isNaN(idNum) || idNum <= 0) {
          res.writeHead(400, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ success: false, errors: ['Poll ID must be a positive integer.'] }));
          return;
        }

        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ success: true, message: `Poll #${idNum} deleted successfully via server AJAX.` }));
      } catch (err) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ success: false, errors: ['Malformed JSON input: ' + err.message] }));
      }
    });
    return;
  }

  // API Route: Save Poll (Create Interview)
  if (req.method === 'POST' && parsedUrl === '/api/save-poll') {
    let body = '';
    req.on('data', chunk => {
      body += chunk.toString();
    });
    req.on('end', () => {
      try {
        if (!body) {
          res.writeHead(400, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ success: false, errors: ['Request body is empty or null.'] }));
          return;
        }

        const poll = JSON.parse(body);
        const errors = [];

        if (!poll || typeof poll !== 'object') {
          errors.push('Invalid JSON payload structure.');
        } else {
          // Title validation
          if (poll.title === undefined || poll.title === null) {
            errors.push('Name poll is missing (null/undefined).');
          } else if (typeof poll.title !== 'string') {
            errors.push('Name poll must be a string.');
          } else {
            const trimmedTitle = poll.title.trim();
            if (trimmedTitle === '') {
              errors.push('Name poll must not be empty or whitespace only.');
            } else if (trimmedTitle.length < 3 || trimmedTitle.length > 255) {
              errors.push('Name poll must be between 3 and 255 characters.');
            }
          }

          // Questions array validation
          if (poll.questions === undefined || poll.questions === null) {
            errors.push('Questions array is missing.');
          } else if (!Array.isArray(poll.questions)) {
            errors.push('Questions must be represented in an array.');
          } else if (poll.questions.length === 0) {
            errors.push('Poll must have at least one question defined.');
          } else {
            poll.questions.forEach((q, idx) => {
              const qNum = idx + 1;
              if (!q || typeof q !== 'object') {
                errors.push(`Question #${qNum} structure is invalid.`);
                return;
              }

              // Question description validation
              if (q.text === undefined || q.text === null) {
                errors.push(`Question #${qNum} description is missing.`);
              } else if (typeof q.text !== 'string') {
                errors.push(`Question #${qNum} description must be a string.`);
              } else {
                const trimmedQ = q.text.trim();
                if (trimmedQ === '') {
                  errors.push(`Question #${qNum} description must not be empty or whitespace only.`);
                } else if (trimmedQ.length < 3 || trimmedQ.length > 255) {
                  errors.push(`Question #${qNum} description must be between 3 and 255 characters.`);
                }
              }

              // Question flags
              if (q.mandatory !== undefined && typeof q.mandatory !== 'boolean') {
                errors.push(`Question #${qNum} 'mandatory' flag must be a boolean.`);
              }
              if (q.multiple !== undefined && typeof q.multiple !== 'boolean') {
                errors.push(`Question #${qNum} 'multiple' flag must be a boolean.`);
              }

              // Question answers validation
              if (q.answers === undefined || q.answers === null) {
                errors.push(`Question #${qNum} answers list is missing.`);
              } else if (!Array.isArray(q.answers)) {
                errors.push(`Question #${qNum} answers must be represented in an array.`);
              } else if (q.answers.length === 0) {
                errors.push(`Question #${qNum} must contain at least one possible answer.`);
              } else {
                q.answers.forEach((ans, ansIdx) => {
                  const ansNum = ansIdx + 1;
                  if (ans === undefined || ans === null) {
                    errors.push(`Question #${qNum} Answer #${ansNum} is missing.`);
                  } else if (typeof ans !== 'string') {
                    errors.push(`Question #${qNum} Answer #${ansNum} must be a string.`);
                  } else {
                    const trimmedAns = ans.trim();
                    if (trimmedAns === '') {
                      errors.push(`Question #${qNum} Answer #${ansNum} must not be empty or whitespace only.`);
                    } else if (trimmedAns.length < 3 || trimmedAns.length > 200) {
                      errors.push(`Question #${qNum} Answer #${ansNum} must be between 3 and 200 characters.`);
                    }
                  }
                });
              }
            });
          }
        }

        if (errors.length > 0) {
          res.writeHead(400, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ success: false, message: 'Server-side validation failed.', errors }));
          return;
        }

        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ success: true, message: 'Poll validated and saved successfully on server.' }));
      } catch (err) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ success: false, errors: ['Malformed JSON input: ' + err.message] }));
      }
    });
    return;
  }

  // Default file resolution
  let filePath = '.' + parsedUrl;
  if (filePath === './') {
    filePath = './index.html';
  }

  const extname = String(path.extname(filePath)).toLowerCase();
  const contentType = MIME_TYPES[extname] || 'application/octet-stream';

  fs.readFile(filePath, (error, content) => {
    if (error) {
      if (error.code === 'ENOENT') {
        // Fall back to index.html for Single Page Application routing if requested path doesn't exist
        fs.readFile('./index.html', (err, indexContent) => {
          if (err) {
            res.writeHead(500);
            res.end(`Server Error: ${err.code}`);
          } else {
            res.writeHead(200, { 'Content-Type': 'text/html' });
            res.end(indexContent, 'utf-8');
          }
        });
      } else {
        res.writeHead(500);
        res.end(`Server Error: ${error.code}`);
      }
    } else {
      res.writeHead(200, { 'Content-Type': contentType });
      res.end(content, 'utf-8');
    }
  });
});

server.listen(PORT, () => {
  console.log(`\x1b[32m[POLLS SERVER] Server running at http://localhost:${PORT}/\x1b[0m`);
  console.log(`\x1b[36m[POLLS SERVER] Click the link above to view your Polls Management System!\x1b[0m`);
});

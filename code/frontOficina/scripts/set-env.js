const fs = require('fs');
const path = require('path');

// Helper to parse .env file
function parseEnv(filePath) {
  if (!fs.existsSync(filePath)) {
    return {};
  }
  const content = fs.readFileSync(filePath, 'utf-8');
  const env = {};
  content.split('\n').forEach(line => {
    const match = line.match(/^\s*([\w.-]+)\s*=\s*(.*)?\s*$/);
    if (match) {
      let value = match[2] || '';
      // Remove surrounding quotes if any
      if (value.length > 0 && value.charAt(0) === '"' && value.charAt(value.length - 1) === '"') {
        value = value.substring(1, value.length - 1);
      } else if (value.length > 0 && value.charAt(0) === "'" && value.charAt(value.length - 1) === "'") {
        value = value.substring(1, value.length - 1);
      }
      env[match[1]] = value.trim();
    }
  });
  return env;
}

const envPath = path.join(__dirname, '../.env');
const dotenv = parseEnv(envPath);

const apiUrlLocal = dotenv.API_URL || 'http://localhost:8080/api';
const apiUrlNetwork = dotenv.API_URL_NETWORK;

const envDir = path.join(__dirname, '../src/environments');
if (!fs.existsSync(envDir)) {
  fs.mkdirSync(envDir, { recursive: true });
}

// Generate environment.ts
const envTsContent = `export const environment = {
  production: false,
  apiUrl: '${apiUrlLocal}'
};
`;
fs.writeFileSync(path.join(envDir, 'environment.ts'), envTsContent);

// Generate environment.development.ts
const envDevTsContent = `export const environment = {
  production: false,
  apiUrl: '${apiUrlLocal}'
};
`;
fs.writeFileSync(path.join(envDir, 'environment.development.ts'), envDevTsContent);

// Generate environment.network.ts
// When API_URL_NETWORK is not configured, the browser host is used automatically.
// This lets phones access the API through the same IP used to open the Angular app.
const envNetworkApiUrl = apiUrlNetwork
  ? `'${apiUrlNetwork}'`
  : `(typeof window !== 'undefined' && window.location.hostname)
    ? \`http://\${window.location.hostname}:8080/api\`
    : 'http://localhost:8080/api'`;

const envNetworkTsContent = `export const environment = {
  production: false,
  apiUrl: ${envNetworkApiUrl}
};
`;
fs.writeFileSync(path.join(envDir, 'environment.network.ts'), envNetworkTsContent);

console.log('Environment files successfully generated from .env file.');

const fs = require('fs');
const path = require('path');
const swaggerPath = path.join(__dirname, 'src/main/resources/api/TMF677-UsageConsumption-v4.0.1.swagger.json');
let swagger = JSON.parse(fs.readFileSync(swaggerPath, 'utf8'));

// Delete the bad paths first
delete swagger.paths['/usageConsumption'];
delete swagger.paths['/usageConsumption/batch'];

// Add new paths correctly for swagger 2.0
swagger.paths['/usageConsumption'] = {
  'post': {
    'tags': ['usageConsumption'],
    'summary': 'Creates a UsageConsumption event',
    'operationId': 'createUsageConsumption',
    'consumes': ['application/json;charset=utf-8'],
    'produces': ['application/json;charset=utf-8'],
    'parameters': [
      {
        'name': 'usageConsumption',
        'in': 'body',
        'required': true,
        'schema': {
          '\': '#/definitions/UsageConsumption'
        }
      }
    ],
    'responses': {
      '201': {
        'description': 'Created',
        'schema': {
          '\': '#/definitions/UsageConsumption'
        }
      }
    }
  }
};

swagger.paths['/usageConsumption/batch'] = {
  'post': {
    'tags': ['usageConsumption'],
    'summary': 'Creates a batch of UsageConsumption events',
    'operationId': 'createUsageConsumptionBatch',
    'consumes': ['application/json;charset=utf-8'],
    'produces': ['application/json;charset=utf-8'],
    'parameters': [
      {
        'name': 'usageConsumptionList',
        'in': 'body',
        'required': true,
        'schema': {
          'type': 'array',
          'items': {
            '\': '#/definitions/UsageConsumption'
          }
        }
      }
    ],
    'responses': {
      '201': {
        'description': 'Created',
        'schema': {
          'type': 'array',
          'items': {
            '\': '#/definitions/UsageConsumption'
          }
        }
      }
    }
  }
};

fs.writeFileSync(swaggerPath, JSON.stringify(swagger, null, 2));
console.log('Swagger patched successfully.');

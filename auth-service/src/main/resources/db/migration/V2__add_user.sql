CREATE USER "${planner_service}" WITH PASSWORD '${planner_password}';
GRANT CONNECT ON DATABASE auth TO "${planner_service}";
GRANT USAGE ON SCHEMA auth.public TO "${planner_service}";
GRANT SELECT ON ALL TABLES IN SCHEMA auth.public TO "${planner_service}";
GRANT SELECT ON ALL SEQUENCES IN SCHEMA auth.public TO "${planner_service}";

ALTER DEFAULT PRIVILEGES IN SCHEMA auth.public GRANT SELECT ON TABLES TO "${planner_service}";
ALTER DEFAULT PRIVILEGES IN SCHEMA auth.public GRANT SELECT ON SEQUENCES TO "${planner_service}";
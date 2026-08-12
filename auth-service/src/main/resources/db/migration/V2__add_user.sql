CREATE USER '${planner_service}' WITH PASSWORD '${planner_password}';
GRANT CONNECT ON DATABASE auth TO planner_service;
GRANT USAGE ON SCHEMA public TO planner_service;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO planner_service;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA public TO planner_service;
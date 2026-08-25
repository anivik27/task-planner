target "auth-service" {
  cache-from = [
    "type=gha,scope=auth-service"
  ]

  cache-to = [
    "type=gha,scope=auth-service,mode=max"
  ]
}

target "planner-service" {
  cache-from = [
    "type=gha,scope=planner-service"
  ]

  cache-to = [
    "type=gha,scope=planner-service,mode=max"
  ]
}

target "mail-sender-service" {
  cache-from = [
    "type=gha,scope=mail-sender-service"
  ]

  cache-to = [
    "type=gha,scope=mail-sender-service,mode=max"
  ]
}

group "default" {
  targets = [
    "auth-service",
    "planner-service",
    "mail-sender-service"
  ]
}
import faust

app = faust.App('dragonguard-jinjin', broker='kafka://kafka:9092', key_serializer='raw')

result_schema = faust.Schema(
    key_type=str,
    value_type=bytes
)

result_topic = app.topic(
    'gitrank.to.scrape.result',
    schema=result_schema,
)

contribution_chema = faust.Schema(
    key_type=str,
    value_type=bytes
)

contribution_topic = app.topic(
    'gitrank.to.scrape.contribution',
    schema=contribution_chema,
)

repo_schema = faust.Schema(
    key_type=str,
    value_type=bytes
)

git_repos_topic = app.topic(
    'gitrank.to.scrape.git-repos',
    schema=repo_schema,
    retention=17
)

issue_schema = faust.Schema(
    key_type=str,
    value_type=bytes
)

issue_topic = app.topic(
    'gitrank.to.scrape.issues',
    schema=issue_schema,
)
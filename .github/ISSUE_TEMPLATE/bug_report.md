---
name: Bug report
about: Create a report to help us improve
title: "[Bug]: "
labels: "버그"
body:
- type: markdown
  attributes:
  value: |
  버그 제보를 해주세요
- type: checkboxes
  attributes:
  label: 이미 제보된 버그인가요?
  description: 이미 제보된 Issue 인지 확인해주세요!
  options:
  - label: 제보된 버그인 지 찾아봤습니다.
    required: true
- type: textarea
    id: what-happened
    attributes:
    label: 무슨 버그인가요?
    description: 기대되는 동작은 무엇인지 설명해주세요
    placeholder: 버그에 대해서 설명해주세요!
    validations:
    required: true
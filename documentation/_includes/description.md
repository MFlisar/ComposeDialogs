{% for key, value in other_projects["libraries"].items() %}
{% for prj in value %}
{% if prj["name"] == project["library"]["name"] %}
{{ prj["description"] }}
{% endif %}
{% endfor %}
{% endfor %}
# Other Libraries

Here you can find an overview of my main libraries that do all play well together including some short descriptions.

{% macro table_row(project) %}
<tr>
{% if project["image"] is defined %}
<td>
<img class="exclude-glightbox" src="{{ project["image"] }}" style="max-height:400px;">
</td>
{% endif %}
<td>
    <a href="{{ project["link"] }}" target="_blank">{{ project["name"] }}</a>
<td>{{ project["description"] }}<br><img class="exclude-glightbox" src="https://img.shields.io/maven-central/v/{{ project["maven"] }}?label=&style=for-the-badge&labelColor=444444&color=grey" /></td>
</tr>
{% endmacro %}

{% for key, value in other_projects["libraries"].items() %}
    {% set ns = namespace(image=false) %}
    {% for prj in value %}
        {% if prj["name"] != project["library"]["name"] and prj["name"] != "Toolbox" %}
            {% if prj["image"] is defined %}
                {% set ns.images = true %}
            {% endif %}
        {% endif %}
    {% endfor %}
<h2>{{key}}</h2>
<table>
<tr>
    {% if ns.images  %}
    <th>Image</th>
    {% endif %}
    <th>Library</th>
    <th>Description</th>
</tr>
{% for prj in value %}
    {% if prj["name"] != project["library"]["name"] and prj["name"] != "Toolbox" %}
        {{ table_row(prj) }}
    {% endif %}
{% endfor %}
</table>
{% endfor %}
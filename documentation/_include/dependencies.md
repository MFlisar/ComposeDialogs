{% if project["dependencies"] is defined and project["dependencies"]["compose"] is defined %}

## Compose

|      Dependency       | Version |                     Infos                      |
|:---------------------:|:-------:|:----------------------------------------------:|
| Compose Multiplatform | `{{ project["dependencies"]["compose"]["cmp"] }}` | Uses compose `{{ project["dependencies"]["compose"]["compose"] }}` and material3 `{{ project["dependencies"]["compose"]["material3"] }}` |

{% if project["dependencies"]["compose"]["experimental"] %}

!!! warning

    I try to use as few experimental APIs as possible, but this library does use a few experimental APIs which are still marked as experimental in material3 `{{ project["dependencies"]["compose"]["material3"] }}`. I will provide new versions as soon as possible if experimental APIs change or become stable.

{% endif %}

{% endif %}

{% macro row_dependencies(dependencies) %}
  {% if dependencies|length > 0 %}
    <td>
      {% for d in dependencies %}
      <a href={{ d["link"] }} target="_blank">{{ d["name"] }}</a><br>
      {% endfor %}
    </td>
    <td>
      {% for d in dependencies %}
      <code>{{ d["version"] }}</code><br>
      {% endfor %}
    </td>
  {% else %}
    <td>-</td>
    <td></td>
  {% endif %}
{% endmacro %}

{% if project["modules"] is not defined or project["modules"] | length == 1 %}
  {% if  project["modules"] is not defined or project["modules"][0]["dependencies"] | length == 0 %}
  This library does not have any dependencies! 
  {% else %}
This library does have following dependencies.
<table>
  <tr>
    <th>Dependency</th>
    <th>Version</th>
  </tr>
  {% for dependency in project["modules"][0]["dependencies"] -%}
    {{ row_dependencies([dependency]) }}
  {% endfor %}
</table>
  {% endif %}
{% else %}

## Modules

<table>
  <tr>
    <th>Module</th>
    <th>Dependency</th>
    <th>Version</th>
  </tr>

  {% for group in project["groups"] %}

    <tr><td colspan="3" style="background-color:var(--md-primary-fg-color--light);">{{ group["label"] }}</td></tr>

    {% for module in project["modules"] %}
      {% if module["group"] == group["name"] %}
          
        <tr>
          <td><code>{{ module["name"] }}</code></td>

          {% if module["dependencies"]|length > 0 %}
            <td>
              {% for d in module["dependencies"] %}
              <a href={{ d["link"] }} target="_blank">{{ d["name"] }}</a><br>
              {% endfor %}
            </td>
            <td>
              {% for d in module["dependencies"] %}
              <code>{{ d["version"] }}</code><br>
              {% endfor %}
            </td>
          {% else %}
            <td>-</td>
            <td></td>
          {% endif %}
        </tr>
          
      {% endif %}
    {% endfor %}

  {% endfor %}

</table>

{% endif %}
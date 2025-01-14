{% if project["library"]["multimodule"] %}

## :material-puzzle: Modules

<table>
  <tr>
    <td>Module</td>
    <td>Info</td>
    <td>Description</td>
  </tr>

  {% for group in project["groups"] %}

    <tr><td colspan="3" style="background-color:var(--md-primary-fg-color--light);">{{ group["label"] }}</td></tr>

    {% for module in project["modules"] %}
      {% if module["name"] is in group["modules"] %}
          
        <tr>
          <td><code>{{ module["name"] }}</code></td>
            <td>
                {% if module["optional"] %}
                Optional
                {% else %}
                {% endif %}
            </td>
            <td>
                {{ module["description"] }}
            </td>
        
        </tr>
          
      {% endif %}
    {% endfor %}

  {% endfor %}

</table>

{% endif %}
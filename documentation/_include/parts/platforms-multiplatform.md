{% if project["library"]["multiplatform"] %}

{% if project["modules"] | length == 1 %}
This is a **KMP (kotlin multiplatform)** library which supports following platforms.
{% else %}
This is a **KMP (kotlin multiplatform)** library and the provided modules do support following platforms.
{% endif %}

<table>
  <tr>
    {% if project["modules"] is defined and project["modules"] | length > 1 %}
    <th>Module</th>
    {% endif %}
    <th>Android</th>
    <th>iOS</th>
    <th>JVM</th>
    <th>Info</th>
  </tr>

  {% if project["modules"] | length == 1 %}

  <tr>
      <td>
        {% if project["modules"][0]["platforms"]["android"] %}✔{% else %}-{% endif %}
      </td>
      <td>
        {% if project["modules"][0]["platforms"]["ios"] %}✔{% else %}-{% endif %}
      </td>
      <td>
        {% if project["modules"][0]["platforms"]["jvm"] %}✔{% else %}-{% endif %}
      </td>
      <td>
        {% if project["modules"][0]["platforms"]["info"] is not none %}
        {{ project["modules"][0]["platforms"]["info"] }}
        {% endif %}
      </td>
  </tr>

  {% elif project["modules"] is defined %}

  {% for group in project["groups"] %}

    <tr><td colspan="5" style="background-color:var(--md-primary-fg-color--light);">{{ group["label"] }}</td></tr>

    {% for module in project["modules"] %}
      {% if module["group"] == group["name"] %}
          
        <tr>
            <td><code>{{ module["name"] }}</code></td>
            <td>
              {% if module["platforms"]["android"] %}✔{% else %}-{% endif %}
            </td>
            <td>
              {% if module["platforms"]["ios"] %}✔{% else %}-{% endif %}
            </td>
            <td>
              {% if module["platforms"]["jvm"] %}✔{% else %}-{% endif %}
            </td>
            <td>
              {% if module["platforms"]["info"] is not none %}
              {{ module["platforms"]["info"] }}
              {% endif %}
            </td>
        </tr>
          
      {% endif %}
    {% endfor %}

  {% endfor %}

  {% endif %}

</table>

{% endif %}
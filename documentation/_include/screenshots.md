{% if project["library"]["screenshots"] | length > 0 %}

## :camera: Screenshots 

{% for screenshot in project["library"]["screenshots"] %}
<table> 
    <thead>
        <tr><th>{{ screenshot["name"] }}</th><th></th></tr>
    </thead>
    <tbody>
        <tr>
        {% for image in screenshot["images"] %}
            <td><img src="{{ image }}" alt="{{ image }}" height="400"></td>
        {% endfor %}
        </tr>
    </tbody>
</table>
{% endfor %}

{% endif %}

<#macro header path categories activeCategory searchLabel filter_name>
    <div class="card-header">
        <div class="d-flex flex-row me-auto">
            <ul class="nav nav-pills card-header-pills d-flex flex-row me-auto">
                <#list categories as category>
                    <li class="nav-item">
                        <a aria-current="true"
                           class="nav-link <#if category == activeCategory>active disabled</#if>"
                           href="${path}?activeCategory=${category}">${category}
                        </a>
                    </li>
                </#list>
            </ul>

            <form class="d-flex">
                <input type="hidden" value="${activeCategory}" name="activeCategory">
                <input class="form-control me-2" type="search" name="filter_name" placeholder="${searchLabel}"
                       aria-label="${searchLabel}"
                       value="<#if filter_name??>${filter_name}</#if>">
                <button class=" btn btn-outline-success" type="submit">Поиск</button>
            </form>
        </div>
    </div>
</#macro>
const app = document.getElementById("app");

const responseHandler = (contentElement, response, uri) => response.text().then((body) => {
    const display = {
	request: uri,
	response: {
	    status: response.status,
	    url: response.url,
	    redirected: response.redirected,
	    body
	}
    };
    contentElement.innerText = JSON.stringify(display, null, 2);
});

const headers = new Headers(
    {
	"REQUIRED_HEADER": "this header is to get past the RequestHeaderAuthentication filter"
	, "LET_ME_IN": "this header is to get past the redirect filter"
    }
);

const buttonsToAdd = [
    {
	title: "create a session",
	onclick: (contentElement) => () => fetch("/authenticationProcessing/auth", { method: "get" })
	    .then((response) => {
		const uri = "/authenticationProcessing/auth";

		responseHandler(contentElement, response, uri)
	    }),
    },
    {
	title: "clear the session",
	onclick: (contentElement) => () => fetch("/clearSession", { method: "get" })
	    .then((response) => {
		if (response.status === 200) {
		    contentElement.innerText = "cleared the session";
		} else {
		    contentElement.innerText = "failed to clear the session";
		}
	    })
    },
    {
	title: "call auth endpoint without header",
	onclick: (contentElement) => () => fetch("/authenticationProcessing/info", { method: "get" })
	    .then((response) => {
		const uri = "/authenticationProcessing/info";
		
		responseHandler(contentElement, response, uri)
	    })
    },
    {
	title: "call auth endpoint with header",
	onclick: (contentElement) => () => fetch("/authenticationProcessing/info", { method: "get", headers })
	    .then((response) => {
		const uri = "/authenticationProcessing/info";
		
		responseHandler(contentElement, response, uri)
	    })
    }
]

const getButtonAddingFunction = (doc) => (parentElement) => (buttonConfiguration) => {
    const button = doc.createElement("button");
    const div = doc.createElement("div");
    const content = doc.createElement("pre");

    button.innerText = buttonConfiguration.title;
    button.onclick = buttonConfiguration.onclick(content);

    div.appendChild(button);
    div.appendChild(content);

    parentElement.appendChild(div);
};

R.forEach(
    getButtonAddingFunction(document)(app),
    buttonsToAdd
);

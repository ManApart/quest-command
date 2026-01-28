const VERSION = "0.0.1";
const CACHE_NAME = `quest-command-${VERSION}`;

const APP_STATIC_RESOURCES = [
    "/index.html",
    "/quest-command.js",
    "/styles.css",
    "/favicon.png",
];

self.addEventListener("install", (event) => {
    event.waitUntil(
        (async () => {
            const cache = await caches.open(CACHE_NAME);
            await cache.addAll(APP_STATIC_RESOURCES);
            console.log("Installed", cache.keys())
        })()
    );
});

self.addEventListener("activate", (event) => {
    event.waitUntil(
        (async () => {
            const names = await caches.keys();
            await Promise.all(
                names.map((name) => {
                    if (name !== CACHE_NAME) {
                        return caches.delete(name);
                    }
                })
            );
            await clients.claim();
        })()
    );
});


self.addEventListener('fetch', function (event) {
    console.log("Fetching", event.request)
    event.respondWith(
        caches.match(event.request).then(function (response) {
            if (response) {
                return response;
            }

            // if not found in cache, return default offline content (only if this is a navigation request)
            if (event.request.mode === 'navigate') {
                console.log("navigating")
                //TODO - this gives connection refused
                caches.match('/games/quest-command/index.html').then(function (response) {
                // caches.match('index.html').then(function (response) {
                    console.log("navigating to index")
                    if (response) {
                        return response;
                    } else {
                        console.log("Fetch Default")
                        return fetch(event.request);
                    }
                })
            } else {
                console.log("Non Navigate")
                return fetch(event.request);
            }

        })
    );
});
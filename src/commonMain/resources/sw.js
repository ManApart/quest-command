const VERSION = "0.0.1";
const CACHE_NAME = `quest-command-${VERSION}`;

const APP_STATIC_RESOURCES = [
    "/games/quest-command/index.html",
    "/games/quest-command/quest-command.js",
    "/games/quest-command/styles.css",
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


self.addEventListener("fetch", (event) => {
    event.respondWith(
        (async () => {
            const cached = await caches.match(event.request);
            if (cached) {
                return cached;
            }

            if (event.request.mode === "navigate") {
                const index = await caches.match("/games/quest-command/index.html");
                if (index) {
                    return index;
                }
            }

            return fetch(event.request);
        })()
    );
});

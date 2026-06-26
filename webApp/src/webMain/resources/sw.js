// Service Worker to optimize loading of large WebAssembly files by serving pre-compressed assets
self.addEventListener('install', event => {
    // Force immediate activation
    self.skipWaiting();
});

self.addEventListener('activate', event => {
    // Claim all clients immediately to intercept fetches on the first visit
    event.waitUntil(self.clients.claim());
});

self.addEventListener('fetch', event => {
    const url = new URL(event.request.url);
    
    // Intercept WebAssembly requests (.wasm)
    if (url.pathname.endsWith('.wasm')) {
        // Skip gzip decompression for local development servers to avoid 404s
        const isLocalhost = location.hostname === 'localhost' || 
                            location.hostname === '127.0.0.1' || 
                            location.hostname.startsWith('192.168.') ||
                            location.hostname.startsWith('10.') ||
                            location.hostname === '[::1]';
        
        if (isLocalhost) {
            console.log('[SW] Localhost detected. Fetching uncompressed WASM directly:', event.request.url);
            event.respondWith(fetch(event.request));
            return;
        }

        const gzipUrl = event.request.url + '.gz';
        console.log('[SW] Intercepted WASM request. Fetching compressed version:', gzipUrl);

        event.respondWith(
            fetch(gzipUrl).then(response => {
                // If the compressed .gz file is not found or fails, fall back to direct uncompressed fetch
                if (!response.ok) {
                    console.warn('[SW] Compressed WASM not found, falling back to raw fetch:', event.request.url);
                    return fetch(event.request);
                }

                // Decompress the stream using the browser's native DecompressionStream API
                const decompressedStream = response.body.pipeThrough(new DecompressionStream('gzip'));

                // Return the decompressed stream with the correct MIME type
                return new Response(decompressedStream, {
                    headers: {
                        'Content-Type': 'application/wasm'
                    }
                });
            }).catch(err => {
                console.error('[SW] Failed to fetch or decompress compressed WASM:', err);
                return fetch(event.request);
            })
        );
    }
});

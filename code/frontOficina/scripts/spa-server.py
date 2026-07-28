import os
import posixpath
import sys
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer
from urllib.parse import unquote, urlparse


class AngularSpaHandler(SimpleHTTPRequestHandler):
    def translate_path(self, path):
        parsed_path = urlparse(path).path
        clean_path = posixpath.normpath(unquote(parsed_path))
        words = [word for word in clean_path.split('/') if word]
        full_path = os.getcwd()

        for word in words:
            drive, word = os.path.splitdrive(word)
            head, word = os.path.split(word)
            if word in (os.curdir, os.pardir):
                continue
            full_path = os.path.join(full_path, word)

        if os.path.exists(full_path):
            return full_path

        if self.command in ('GET', 'HEAD'):
            return os.path.join(os.getcwd(), 'index.html')

        return full_path


def main():
    directory = sys.argv[1] if len(sys.argv) > 1 else os.path.join('dist', 'frontOficina', 'browser')
    port = int(sys.argv[2]) if len(sys.argv) > 2 else 8000

    if not os.path.isdir(directory):
        raise SystemExit('Diretorio nao encontrado: {0}'.format(directory))

    os.chdir(directory)
    server_address = ('0.0.0.0', port)
    httpd = ThreadingHTTPServer(server_address, AngularSpaHandler)

    print('Servindo Angular SPA em http://localhost:{0}'.format(port))
    print('Diretorio: {0}'.format(os.getcwd()))
    httpd.serve_forever()


if __name__ == '__main__':
    main()

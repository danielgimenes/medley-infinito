import argparse
import psycopg2


def insert(filepath, key_id, tempo, name, cover, artist, right_key, duration):
    filename = "_".join(filepath.split("/")[-1].split("_")[:-1])
    conn = psycopg2.connect(
        "host=localhost dbname=medleyinfinito_db user=postgres password=cogitoR341"
    )
    cur = conn.cursor()
    cur.execute(
        "INSERT INTO parts (filepath, keynote, tempo, originalfile, name, cover, artist, right_key, duration) VALUES ('{}', {}, {}, '{}', '{}', '{}', '{}', '{}', '{}', {});".format(
            filepath, key_id, tempo, filename, name, cover, artist, right_key, duration
        )
    )
    conn.commit()
    cur.close()
    conn.close()


def doesnt_exist(filename):
    conn = psycopg2.connect(
        "host=localhost dbname=medleyinfinito_db user=postgres password=cogitoR341"
    )
    cur = conn.cursor()
    cur.execute("SELECT 1 FROM parts WHERE filepath = '{}';".format(filename))
    result = cur.fetchone()
    cur.close()
    conn.close()
    if result is not None:
        return False
    else:
        return True


def update(filepath, name, cover, artist, right_key, duration):
    conn = psycopg2.connect(
        "host=localhost dbname=medleyinfinito_db user=postgres password=cogitoR341"
    )
    cur = conn.cursor()
    cur.execute("UPDATE parts SET name = '{}', cover = '{}', artist = '{}', right_key = '{}', duration = {} WHERE filepath = '{}';".format(name, cover, artist, right_key, duration, filename))
    conn.commit()
    cur.close()
    conn.close()


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("filepath", type=str)
    parser.add_argument("-k", type=int, help="key id")
    parser.add_argument("-t", type=int, help="tempo")

    args = parser.parse_args()

    insert(args.filepath, args.k, args.t)

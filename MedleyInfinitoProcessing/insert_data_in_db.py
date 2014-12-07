import argparse
import psycopg2


def insert(filepath, key_id, tempo):
    filename = "_".join(filepath.split("/")[-1].split("_")[:-1])
    conn = psycopg2.connect(
        "host=localhost dbname=medleyinfinito_db user=postgres password=cogitoR341"
    )
    cur = conn.cursor()
    cur.execute(
        "INSERT INTO parts (
        filepath, keynote, tempo, originalfile
        ) VALUES ('{}', {}, {}, '{}');".format(
            filepath, key_id, tempo, filename
        )
    )
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

import psycopg2


def insert(filepath, key_id, tempo):
    conn = psycopg2.connect("dbname=medleyinfinito_db user=postgres password=cogitoR341")
    cur = conn.cursor()
    cur.execute(
        "INSERT INTO parts (filepath, keynote, tempo) VALUES ('{}', {}, {});".format(
            filepath, key_id, tempo
        )
    )
    cur.close()
    conn.close()

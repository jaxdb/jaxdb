CREATE OR REPLACE FUNCTION reset_sequence(sequence_name IN VARCHAR2, start_with NUMBER) RETURN NUMBER
AS
  query VARCHAR2(255);
  min_value NUMBER;
  max_value NUMBER;
  increment_by NUMBER;
  cycle_flag VARCHAR(1);
  order_flag VARCHAR(1);
  cache_size NUMBER;
  last_number NUMBER;
  currval NUMBER;
  diffval NUMBER;
BEGIN

  query := 'SELECT min_value, max_value, increment_by, cycle_flag, order_flag, cache_size, last_number FROM all_sequences WHERE sequence_name = ''' || UPPER(sequence_name) || '''';
  -- DBMS_OUTPUT.PUT_LINE(query);
  EXECUTE IMMEDIATE query INTO min_value, max_value, increment_by, cycle_flag, order_flag, cache_size, last_number;
  -- DBMS_OUTPUT.PUT_LINE(min_value || ' ' || max_value || ' ' || increment_by || ' ' || cycle_flag || ' ' || order_flag || ' ' || cache_size || ' ' || last_number);

  IF start_with < min_value THEN

    RAISE_APPLICATION_ERROR(-20001, 'sequence ' || sequence_name || '.NEXTVAL goes below MINVALUE');

  ELSE

    -- Get the current sequence value;
    query := 'SELECT ' || sequence_name || '.NEXTVAL FROM dual';
    -- DBMS_OUTPUT.PUT_LINE(query);
    EXECUTE IMMEDIATE query INTO currval;
    -- DBMS_OUTPUT.PUT_LINE(currval);

    EXECUTE IMMEDIATE 'DROP SEQUENCE ' || sequence_name;
    query := 'CREATE SEQUENCE ' || sequence_name || ' INCREMENT BY ' || increment_by || ' START WITH ' || start_with;

    IF max_value IS NOT NULL THEN
      query := query || ' MAXVALUE ' || max_value;
    ELSE
      query := query || ' NOMAXVALUE';
    END IF;

    IF min_value IS NOT NULL THEN
      query := query || ' MINVALUE ' || min_value;
    ELSE
      query := query || ' NOMINVALUE';
    END IF;

    IF UPPER(cycle_flag) = 'Y' THEN
      query := query || ' CYCLE';
    ELSE
      query := query || ' NOCYCLE';
    END IF;

    IF cache_size > 0 THEN
      query := query || ' CACHE ' || cache_size;
    ELSE
      query := query || ' NOCACHE';
    END IF;

    IF UPPER(order_flag) = 'Y' THEN
      query := query || ' ORDER';
    ELSE
      query := query || ' NOORDER';
    END IF;

    -- DBMS_OUTPUT.PUT_LINE(query);
    EXECUTE IMMEDIATE query;

    RETURN 1;

  END IF;

EXCEPTION
  WHEN no_data_found THEN
    RETURN 0;

END reset_sequence;
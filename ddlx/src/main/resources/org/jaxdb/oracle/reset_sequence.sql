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
  diffval NUMBER;
BEGIN

  BEGIN

    query := 'SELECT min_value, max_value, increment_by, cycle_flag, order_flag, cache_size, last_number FROM all_sequences WHERE sequence_name = ''' || sequence_name || '''';
    -- DBMS_OUTPUT.PUT_LINE(query);
    EXECUTE IMMEDIATE query INTO min_value, max_value, increment_by, cycle_flag, order_flag, cache_size, last_number;
    -- DBMS_OUTPUT.PUT_LINE(min_value || ' ' || max_value || ' ' || increment_by || ' ' || cycle_flag || ' ' || order_flag || ' ' || cache_size || ' ' || last_number);

  EXCEPTION WHEN no_data_found THEN

    DECLARE
      sequence_does_not_exist EXCEPTION;
      PRAGMA exception_init(sequence_does_not_exist, -02289);
    BEGIN

      -- Get the current sequence value;
      query := 'SELECT "' || sequence_name || '".NEXTVAL FROM dual';
      -- DBMS_OUTPUT.PUT_LINE(query);
      EXECUTE IMMEDIATE query;

      query := 'SELECT min_value, max_value, increment_by, cycle_flag, order_flag, cache_size, last_number FROM all_sequences WHERE sequence_name = ''' || sequence_name || '''';
      -- DBMS_OUTPUT.PUT_LINE(query);
      EXECUTE IMMEDIATE query INTO min_value, max_value, increment_by, cycle_flag, order_flag, cache_size, last_number;
      -- DBMS_OUTPUT.PUT_LINE(min_value || ' ' || max_value || ' ' || increment_by || ' ' || cycle_flag || ' ' || order_flag || ' ' || cache_size || ' ' || last_number);

    EXCEPTION WHEN sequence_does_not_exist THEN
      RETURN 0;

    END;
  END;

  IF start_with < min_value THEN

    RAISE_APPLICATION_ERROR(-20001, 'sequence "' || sequence_name || '".NEXTVAL goes below MINVALUE');

  ELSE

    EXECUTE IMMEDIATE 'DROP SEQUENCE "' || sequence_name || '"';
    query := 'CREATE SEQUENCE "' || sequence_name || '" INCREMENT BY ' || increment_by || ' START WITH ' || start_with;

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

END reset_sequence;

-- DROP SEQUENCE "sq_type_id";
-- CREATE SEQUENCE "sq_type_id" INCREMENT BY 1 START WITH 1001 MAXVALUE 1000000 MINVALUE 0 CYCLE CACHE 1000000 NOORDER;

-- EXEC DBMS_OUTPUT.PUT_LINE(reset_sequence('sq_type_id', 1000));
-- SELECT "sq_type_id".CURRVAL FROM dual;
-- SELECT "sq_type_id".NEXTVAL FROM dual;
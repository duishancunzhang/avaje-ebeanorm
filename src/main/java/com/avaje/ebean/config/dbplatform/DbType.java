package com.avaje.ebean.config.dbplatform;

/**
 * Represents a DB type with name, length, precision, and scale.
 * <p>
 * The length is for VARCHAR types and precision/scale for DECIMAL types.
 * </p>
 */
public class DbType {

  /**
   * DB native UUID type (H2 and Postgres).
   */
  public static final int UUID = 5010;

  /**
   * Type to map Map content to Postgres HSTORE.
   */
  public static final int HSTORE = 5000;

  /**
   * Type to map JSON content to Clob or Postgres JSON type.
   */
  public static final int JSON = 5001;

  /**
   * Type to map JSON content to Clob or Postgres JSONB type.
   */
  public static final int JSONB = 5002;

  /**
   * Type to map JSON content to VARCHAR.
   */
  public static final int JSONVarchar = 5003;

  /**
   * Type to map JSON content to Clob.
   */
  public static final int JSONClob = 5004;

  /**
   * Type to map JSON content to Blob.
   */
  public static final int JSONBlob = 5005;

  /**
   * The data type name (VARCHAR, INTEGER ...)
   */
  private final String name;

  /**
   * The default length or precision.
   */
  private final int defaultLength;

  /**
   * The default scale (decimal).
   */
  private final int defaultScale;

  /**
   * Set to true if the type should never have a length or scale.
   */
  private final boolean canHaveLength;

  /**
   * Construct with no length or scale.
   */
  public DbType(String name) {
    this(name, 0, 0);
  }

  /**
   * Construct with a given length.
   */
  public DbType(String name, int defaultLength) {
    this(name, defaultLength, 0);
  }

  /**
   * Construct for Decimal with precision and scale.
   */
  public DbType(String name, int defaultPrecision, int defaultScale) {
    this.name = name;
    this.defaultLength = defaultPrecision;
    this.defaultScale = defaultScale;
    this.canHaveLength = true;
  }

  /**
   * Use with canHaveLength=false for types that should never have a length.
   * 
   * @param name
   *          the type name
   * @param canHaveLength
   *          set this to false for type that should never have a length
   */
  public DbType(String name, boolean canHaveLength) {
    this.name = name;
    this.defaultLength = 0;
    this.defaultScale = 0;
    this.canHaveLength = canHaveLength;
  }

  /**
   * Return the type for a specific property that incorporates the name, length,
   * precision and scale.
   * <p>
   * The deployLength and deployScale are for the property we are rendering the
   * DB type for.
   * </p>
   * 
   * @param deployLength
   *          the length or precision defined by deployment on a specific
   *          property.
   * @param deployScale
   *          the scale defined by deployment on a specific property.
   */
  public String renderType(int deployLength, int deployScale) {
    return renderType(deployLength, deployScale, true);
  }

  /**
   * Render the type defining strict mode.
   * <p>
   * If strict mode if OFF then this will render with a scale value even if
   * that is not strictly supported. The reason for supporting this is to enable
   * use to use types like jsonb(200) as a "logical" type that maps to JSONB for
   * Postgres and VARCHAR(200) for other databases.
   * </p>
   */
  public String renderType(int deployLength, int deployScale, boolean strict) {

    StringBuilder sb = new StringBuilder();
    sb.append(name);

    if (canHaveLength || !strict) {
      // see if there is a precision/scale to add (or not)
      int len = deployLength != 0 ? deployLength : defaultLength;
      if (len > 0) {
        sb.append("(");
        sb.append(len);
        int scale = deployScale != 0 ? deployScale : defaultScale;
        if (scale > 0) {
          sb.append(",");
          sb.append(scale);
        }
        sb.append(")");
      }
    }

    return sb.toString();
  }

  /**
   * Create a copy of the type with a new default length.
   */
  public DbType withLength(int defaultLength) {
    return new DbType(name, defaultLength);
  }
}

class Some {
    var a : Int
        get() = 0
        <caret>
}

// EXIST:  abstract
// EXIST:  annotation
// EXIST:  as
// EXIST:  class
// EXIST:  enum
// EXIST:  final
// EXIST:  fun
// EXIST:  get
/*TODO*/
// EXIST:  in
/*why?*/
// EXIST:  inner
// EXIST:  internal
// EXIST:  object
// EXIST:  open
// EXIST:  out
/*why?*/
// EXIST:  override
// EXIST:  private
// EXIST:  protected
// EXIST:  public
// EXIST: set
// EXIST:  trait
// EXIST:  val
// EXIST:  var
// EXIST:  vararg
/*why?*/
// EXIST:  constructor
// EXIST:  init
// NUMBER: 26
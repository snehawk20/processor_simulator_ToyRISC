	.data
n:
	0
l:

	.text
main:
	add %x0, %x0, %x3            @x3 = array index number, x10 stores the ans
	add %x0, %x0, %x10
	load %x0, $n, %x8            @load n into x8
loop:
	load %x3, $l, %x4			 @load array elements into x4
	andi %x4, 1, %x6			 @x6 = 0 if element is even,else odd 
	srai %x4, 31, %x7            @checking the MSB, if its 0 then element is pos., else neg.
	or %x7, %x6, %x9
	beq %x9, %x0, success		 @x9 = 0 only when element is positive(>=0) even number
	addi %x3, 1, %x3			 @incrementing array index
	beq %x3, %x8, endl	         @condition to end
	jmp loop	 				 @jump back into loop
success:
	addi %x10, 1, %x10		     @incrementing ans
	addi %x3, 1, %x3             @incrementing array index
	beq %x3, %x8, endl           @condition to end
	jmp loop                     @jump back into loop
endl:
	end
